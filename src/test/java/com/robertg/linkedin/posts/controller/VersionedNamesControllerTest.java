package com.robertg.linkedin.posts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertg.linkedin.posts.dto.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class VersionedNamesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void namesV1() throws Exception {
        final var names = List.of("John", "Olivia", "Liam", "Sophia");
        final var expectedNames = objectMapper.writeValueAsString(names);

        this.mockMvc.perform(get("/api/v1/names"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedNames));
    }

    @Test
    void namesV2() throws Exception {
        final var names = List.of(new Name("John"), new Name("Olivia"), new Name("Liam"), new Name("Sophia"));
        final var expectedNames = objectMapper.writeValueAsString(names);

        this.mockMvc.perform(get("/api/v2/names"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedNames));
    }
}