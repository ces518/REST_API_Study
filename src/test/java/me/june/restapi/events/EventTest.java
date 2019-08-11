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

    @Test
    public void testFree () {
        // given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isTrue();

        // given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline () {
        // given
        Event event = Event.builder()
                .location("대전 둔산동")
                .build();

        // when
        event.update();

        // then
        assertThat(event.isOffline()).isTrue();

        // given
        event = Event.builder()
                .location(null)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isOffline()).isFalse();
    }

}
