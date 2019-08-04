package me.june.restapi.events;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder () {
        Event event = Event.builder()
                .name("Event REST API !! ")
                .description("REST API DEV With Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean () {
        Event event = new Event();
        String description = "Spring";
        String eventName = "Event";

        event.setName(eventName);
        event.setDescription(description);

    }
}
