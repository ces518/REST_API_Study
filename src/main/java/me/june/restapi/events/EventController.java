package me.june.restapi.events;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.june.restapi.accounts.Account;
import me.june.restapi.accounts.AccountAdapter;
import me.june.restapi.accounts.CurrentUser;
import me.june.restapi.common.ErrorResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-05
 * Time: 22:07
 **/
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final ObjectMapper objectMapper;
    private final EventRepository eventRepository;
    private final EventValidator eventValidator;

    public EventController(ObjectMapper objectMapper, EventRepository eventRepository, EventValidator eventValidator) {
        this.objectMapper = objectMapper;
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
    }

    @GetMapping
    public ResponseEntity getEvents (Pageable pageable,
                                     PagedResourcesAssembler<Event> assembler,
//                                     @AuthenticationPrincipal AccountAdapter currentUser
//                                     @AuthenticationPrincipal(expression = "account") Account account
                                     @CurrentUser Account account
        ) { // paging과 관련된 파라메터들을 받아올 수 있음.
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        */
        Page<Event> pagedEvents = this.eventRepository.findAll(pageable);
        PagedResources<Resource<Event>> pagedResources = assembler.toResource(pagedEvents, e -> new EventResource(e));

        // 인가된 사용자의 경우 이벤트 생성 링크 제공
        if (account != null) {
            pagedResources.add(linkTo(EventController.class).withRel("create-event"));
        }

        pagedResources.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getEvent (@PathVariable Integer id, @CurrentUser Account account) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));

        // 인가된 사용자이고, 해당 이벤트의 오너일경우 이벤트 수정 링크 제공
        if (event.getManager().equals(account)) {
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(eventResource);
    }

    @PostMapping
    public ResponseEntity createEvent (@Valid @RequestBody EventDto eventDto,
                                       Errors errors,
                                       @CurrentUser Account account) { // 입력값을 EventDto를 활용하여 받는다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = objectMapper.convertValue(eventDto, Event.class);
        // 비즈니스 로직을 수행
        event.update();

        // 인가된 사용자일 경우 event의 오너로 지정
        if (account != null) {
            event.setManager(account);
        }

        Event savedEvent = eventRepository.save(event);
        // created를 생성할때는 항상 uri를 제공해야한다.
        // org.springframework.hateoas.mvc.ControllerLinkBuilder 를 사용하면 손쉽게 URI를 생성할 수 있음.
        ControllerLinkBuilder linkBuilder = linkTo(EventController.class).slash(savedEvent.getId()); // 새로 생성된 Event의 ID를 기반으로 Location Header로 들어간다.
        URI uri = linkBuilder.toUri();

        /* 링크정보에는 어떤 Method를 사용해야하는지에 대한 정보는 담을 수 없다. */
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        // eventResource.add(linkBuilder.withSelfRel());
        eventResource.add(linkBuilder.withRel("update-event"));
        // profile Link 추가
        eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(uri).body(eventResource);
    }

    @PutMapping("{id}")
    public ResponseEntity updateEvent (@PathVariable Integer id,
                                       @Valid @RequestBody EventDto eventDto,
                                       Errors errors,
                                       @CurrentUser Account account) throws JsonMappingException {
        // 이벤트가 존재하지 않는경우 404
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // 바인딩이 맞지않는경우 400
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        // 비지니스 로직상 맞지않은경우 400
        this.eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existEvent = optionalEvent.get();

        // 현재 사용자가 이벤트의 오너가 아닐경우 UNAUTHORIZED 응답
        if (!existEvent.getManager().equals(account)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Event event = this.objectMapper.updateValue(existEvent, eventDto);
        Event savedEvent = this.eventRepository.save(event);
        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("/doc/index.html#resources-events-update").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity<ErrorResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
