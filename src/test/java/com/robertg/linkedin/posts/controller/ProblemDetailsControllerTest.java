package com.robertg.linkedin.posts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ProblemDetailsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testProblemDetailEndpoint() throws Exception {
        final var problemDetail = ProblemDetail.forStatus(429);
        problemDetail.setInstance(URI.create("/api/v1/problem-details"));
        final var expectedContent = objectMapper.writeValueAsString(problemDetail);

        this.mockMvc.perform(get("/api/v1/problem-details"))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(content().string(expectedContent));
    }

    @Test
    void testNameEndpointWithNonExistingId() throws Exception {
        final var problemDetail = ProblemDetail.forStatus(404);
        problemDetail.setDetail("The requested name doesn't exist.");
        problemDetail.setInstance(URI.create("/api/v1/problem-details/names/101"));
        final var expectedContent = objectMapper.writeValueAsString(problemDetail);

        this.mockMvc.perform(get("/api/v1/problem-details/names/101"))
                .andExpect(status().isNotFound())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(content().string(expectedContent));
    }
}