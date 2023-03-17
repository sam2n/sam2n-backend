package com.sam2n.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam2n.backend.config.SecurityConfig;
import com.sam2n.backend.domain.Activity;
import com.sam2n.backend.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
@Import(SecurityConfig.class)
class ActivityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ActivityService activityService;
    @Test
    void getActivity() throws Exception {
        Activity activity = new Activity();

        given(activityService.addNewActivity(any()))
                .willReturn(activity);

        mockMvc.perform(post("/api/v1/activity")
                        .with(httpBasic("user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new URL("https://google.com"))))
                .andExpect(status().isOk());
    }
}