package me.june.restapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-10
 * Time: 23:25
 **/
@Component
public class EventValidator{

    public void validate (EventDto eventDto, Errors errors) {
        // 무제한 경매가 아닌데, basePrice 가 max보다 큰 경우
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            // fieldError
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is must be less than MaxPrice");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime != null) {
            if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                    endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                    endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
                errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is must be more than begin");
            }
        }
    }
}
