package me.june.restapi.index;

import me.june.restapi.events.EventController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-19
 * Time: 19:04
 **/
@RestController
public class IndexController {

    @GetMapping("/api")
    public ResourceSupport index () {
        ResourceSupport index = new ResourceSupport();
        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}
