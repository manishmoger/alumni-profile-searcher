package com.alumni.search;

import com.alumni.search.controller.AlumniController;
import com.alumni.search.model.AlumniProfile;
import com.alumni.search.service.AlumniService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlumniController.class)
class AlumniControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlumniService alumniService;

    private final List<AlumniProfile> mockProfiles = List.of(
            new AlumniProfile("Rahul Mehta", "Software Engineer", "IIT Bombay", "Bangalore, India", "Software Engineer at Google", 2020)
    );

    @Test
    void search_withValidRequest_shouldReturn200() throws Exception {
        when(alumniService.searchAndSave(any())).thenReturn(mockProfiles);

        mockMvc.perform(post("/api/alumni/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("university", "IIT Bombay", "designation", "Software Engineer"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data[0].name").value("Rahul Mehta"));
    }

    @Test
    void search_withMissingUniversity_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/alumni/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("designation", "Backend Developer"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    void search_withMissingDesignation_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/alumni/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("university", "IIT Bombay"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    void getAll_shouldReturn200WithProfiles() throws Exception {
        when(alumniService.getAllProfiles()).thenReturn(mockProfiles);

        mockMvc.perform(get("/api/alumni/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data[0].currentRole").value("Software Engineer"));
    }

    @Test
    void search_withOptionalPassoutYear_shouldReturn200() throws Exception {
        when(alumniService.searchAndSave(any())).thenReturn(mockProfiles);

        mockMvc.perform(post("/api/alumni/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("university", "IIT Bombay", "designation", "Software Engineer", "passoutYear", "2020"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }
}
