package me.june.restapi.events;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-07
 * Time: 21:31
 **/
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class EventDto {
    private String name;

    private String description;

    private LocalDateTime beginEnrollmentDateTime;

    private LocalDateTime closeEnrollmentDateTime;

    private LocalDateTime beginEventDateTime;

    private LocalDateTime endEventDateTime;

    private String location; // optional 없다면 온라인모임

    private int basePrice;

    private int maxPrice;

    private int limitOfEnrollment;
}
