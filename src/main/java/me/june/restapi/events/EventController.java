package me.june.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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

    public EventController(ObjectMapper objectMapper, EventRepository eventRepository) {
        this.objectMapper = objectMapper;
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity createEvent (@RequestBody EventDto eventDto) { // 입력값을 EventDto를 활용하여 받는다.

        Event event = objectMapper.convertValue(eventDto, Event.class);

        Event savedEvent = eventRepository.save(event);
        // created를 생성할때는 항상 uri를 제공해야한다.
        // org.springframework.hateoas.mvc.ControllerLinkBuilder 를 사용하면 손쉽게 URI를 생성할 수 있음.
        ControllerLinkBuilder linkBuilder = linkTo(EventController.class).slash(savedEvent.getId()); // 새로 생성된 Event의 ID를 기반으로 Location Header로 들어간다.
        URI uri = linkBuilder.toUri();
        return ResponseEntity.created(uri).body(savedEvent);
    }
}
