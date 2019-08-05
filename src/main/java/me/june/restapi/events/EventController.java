package me.june.restapi.events;

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
@RequestMapping(value = "/api/events", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class EventController {

    @PostMapping
    public ResponseEntity createEvent (@RequestBody Event event) {

        // created를 생성할때는 항상 uri를 제공해야한다.
        // org.springframework.hateoas.mvc.ControllerLinkBuilder 를 사용하면 손쉽게 URI를 생성할 수 있음.
        ControllerLinkBuilder linkBuilder = linkTo(EventController.class).slash("{id}");
        event.setId(10);
        URI uri = linkBuilder.toUri();
        return ResponseEntity.created(uri).body(event);
    }
}
