package me.june.restapi.events;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime beginEnrollmentDateTime;

    @NotNull
    private LocalDateTime closeEnrollmentDateTime;

    @NotNull
    private LocalDateTime beginEventDateTime;

    @NotNull
    private LocalDateTime endEventDateTime;

    private String location; // optional 없다면 온라인모임

    @Min(0)
    private int basePrice;

    @Min(0)
    private int maxPrice;

    @Min(0)
    private int limitOfEnrollment;
}
