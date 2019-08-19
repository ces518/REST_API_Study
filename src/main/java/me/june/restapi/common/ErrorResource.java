package me.june.restapi.common;

import me.june.restapi.index.IndexController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-19
 * Time: 19:11
 **/
public class ErrorResource extends Resource<Errors> {

    public ErrorResource(Errors content, Link... links) {
        super(content, links);
        /* Error Resource 로 변경하여 index에 대한 링크정보를 함께 제공*/
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
