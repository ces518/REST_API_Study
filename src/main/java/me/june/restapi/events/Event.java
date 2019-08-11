package me.june.restapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-04
 * Time: 22:32
 **/
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

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

    private boolean offline;

    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = Boolean.TRUE;
        } else {
            this.free = Boolean.FALSE;
        }
        if (this.location.trim().isEmpty()) {
            this.offline = Boolean.FALSE;
        } else {
            this.offline = Boolean.TRUE;
        }
    }
}
