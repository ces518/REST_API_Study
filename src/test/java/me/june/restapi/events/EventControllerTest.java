package me.june.restapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.june.restapi.common.RestDocsConfiguration;
import me.june.restapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
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

        String eventJsonString = objectMapper.writeValueAsString(event);

        this.mockMvc.perform(post("/api/events")
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
                    .andExpect(jsonPath("$.eventStatus").value(EventStatus.DRAFT.name()))
                    .andExpect(jsonPath("$._links.self").exists())
                    .andExpect(jsonPath("$._links.query-events").exists())
                    .andExpect(jsonPath("$._links.update-event").exists())
                    .andDo(document("create-event",
                            links(
                                    linkWithRel("self").description("link to self"),
                                    linkWithRel("query-events").description("link to query events"),
                                    linkWithRel("update-event").description("link to update event"),
                                    linkWithRel("profile").description("link to profile")
                            ),
                            requestHeaders(
                                    headerWithName(HttpHeaders.ACCEPT).description("Accept Header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type Header")
                            ),
//                            requestFields(
                            relaxedRequestFields(
                                    fieldWithPath("name").description("name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("basePrice of new event"),
                                    fieldWithPath("maxPrice").description("maxPrice of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of new event")
                            ),
                            responseHeaders(
                                    headerWithName(HttpHeaders.LOCATION).description("Location Header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type Header")
                            ),
                            responseFields(
//                            relaxedResponseFields(
                                    fieldWithPath("id").description("identifier of new event"),
                                    fieldWithPath("name").description("name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("basePrice of new event"),
                                    fieldWithPath("maxPrice").description("maxPrice of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of new event"),
                                    fieldWithPath("free").description("it tells if this event is free or not"),
                                    fieldWithPath("offline").description("it tells if this events is offline or not"),
                                    fieldWithPath("eventStatus").description("event status"),
                                    fieldWithPath("_links.self.href").description("link to self"),
                                    fieldWithPath("_links.query-events.href").description("link to query-events"),
                                    fieldWithPath("_links.update-event.href").description("link to update-event"),
                                    fieldWithPath("_links.profile.href").description("link to profile")
                            )
                    ))
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
                        .content(objectMapper.writeValueAsString(eventDto))
                    )
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
