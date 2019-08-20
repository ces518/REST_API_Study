package me.june.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.june.restapi.common.ErrorResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getEvents (Pageable pageable, PagedResourcesAssembler<Event> assembler) { // paging과 관련된 파라메터들을 받아올 수 있음.
        Page<Event> pagedEvents = this.eventRepository.findAll(pageable);
        PagedResources<Resource<Event>> pagedResources = assembler.toResource(pagedEvents, e -> new EventResource(e));
        pagedResources.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getEvent (@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    @PostMapping
    public ResponseEntity createEvent (@Valid @RequestBody EventDto eventDto, Errors errors) { // 입력값을 EventDto를 활용하여 받는다.
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }

        Event event = objectMapper.convertValue(eventDto, Event.class);
        // 비즈니스 로직을 수행
        event.update();

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
}
