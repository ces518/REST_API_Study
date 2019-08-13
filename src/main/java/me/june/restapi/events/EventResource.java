package me.june.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-13
 * Time: 21:35
 **/
@Getter
public class EventResource extends Resource<Event> {

    @JsonUnwrapped
    private Event event;

    public EventResource(Event content, Link... links) {
        super(content, links);
        // self에 대한 링크를 EventResource 생성자에서 추가
        add(linkTo(EventController.class).slash(content.getId()).withSelfRel());
    }

//    public EventResource(Event event) {
//        this.event = event;
//    }
}
