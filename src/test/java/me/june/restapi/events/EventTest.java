package me.june.restapi.events;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
//    @Parameters({
//            "0, 0, true",
//            "100, 0, false",
//            "0, 100, false"
//    })
    @Parameters(method = "paramsForTestFree")
    public void testFree (int basePrice, int maxPrice, boolean isFree) {
        // given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private Object[] paramsForTestFree () {
        return new Object[] {
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {100, 200, false}
        };
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
