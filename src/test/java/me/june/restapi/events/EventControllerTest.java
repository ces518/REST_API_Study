package me.june.restapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.june.restapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상적인 이벤트 생성 테스트")
    public void 이벤트생성_테스트 () throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Study")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 8 , 5, 11, 23))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8 , 5, 11, 23))
                .beginEventDateTime(LocalDateTime.of(2019, 8, 15, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 16, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("대전 둔산동 스타벅스")
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        event.setId(100);

        // EventRepository를 Mocking했기때문에 return값을 mocking해주어야함
//        given(eventRepository.save(any(Event.class))).willReturn(event);

        String eventJsonString = objectMapper.writeValueAsString(event);

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON_UTF8)
                        .content(eventJsonString)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated()) // 201 응답
                    .andExpect(header().exists(HttpHeaders.LOCATION))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.id").value(Matchers.not(100))) // 입력값이 들어와선 안된다.
                    .andExpect(jsonPath("$.free").value(false)) // 유료 이벤트
                    .andExpect(jsonPath("$.offline").value(true)) // 오프라인
                    .andExpect(jsonPath("$.eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
    public void 이벤트생성_테스트_이외의값_에러발생 () throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Study")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 8 , 5, 11, 23))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8 , 5, 11, 23))
                .beginEventDateTime(LocalDateTime.of(2019, 8, 15, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 16, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("대전 둔산동 스타벅스")
                .free(true)
                .offline(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        event.setId(100);

        String eventJsonString = objectMapper.writeValueAsString(event);

        this.mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON_UTF8)
                        .content(eventJsonString)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())// BAD_REQUEST 응답
                ;
    }

    @Test
    public void 이벤트생성_입력값이_없을경우_BAD_REQUEST () throws Exception {
        // 입력값을 아무것도 보내지않을 경우 테스트
        EventDto eventDto = EventDto.builder()
                .build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(eventDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("이벤트 생성 시작일이 종료일을 넘을경우 테스트")
    public void 이벤트생성_시작일이_종료일을_넘을경우_BAD_REQUEST () throws Exception {
        // 입력값을 아무것도 보내지않을 경우 테스트
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Study")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 10 , 5, 11, 23))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 8 , 5, 11, 23))
                .beginEventDateTime(LocalDateTime.of(2019, 10, 15, 14, 21))
                .endEventDateTime(LocalDateTime.of(2019, 8, 16, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("대전 둔산동 스타벅스")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }

}
