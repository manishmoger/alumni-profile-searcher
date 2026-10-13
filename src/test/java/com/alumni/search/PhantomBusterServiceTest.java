package com.alumni.search;

import com.alumni.search.dto.SearchRequest;
import com.alumni.search.exception.PhantomBusterException;
import com.alumni.search.service.PhantomBusterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhantomBusterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private PhantomBusterService service;

    @BeforeEach
    void setUp() {
        service = new PhantomBusterService(restTemplate, new ObjectMapper());
        ReflectionTestUtils.setField(service, "apiKey", "test-key");
        ReflectionTestUtils.setField(service, "agentId", "test-agent");
        ReflectionTestUtils.setField(service, "baseUrl", "http://mock-phantombuster");
    }

    @Test
    void fetchAlumniProfiles_whenApiThrows_shouldThrowPhantomBusterException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RestClientException("Connection refused"));

        SearchRequest request = new SearchRequest();
        request.setUniversity("IIT Bombay");
        request.setDesignation("Software Engineer");

        assertThatThrownBy(() -> service.fetchAlumniProfiles(request))
                .isInstanceOf(PhantomBusterException.class)
                .hasMessageContaining("Failed to communicate with PhantomBuster API");
    }

    @Test
    void fetchAlumniProfiles_whenOutputNeverReady_shouldThrowPhantomBusterException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("launched"));
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));

        SearchRequest request = new SearchRequest();
        request.setUniversity("IIT Bombay");
        request.setDesignation("Software Engineer");

        assertThatThrownBy(() -> service.fetchAlumniProfiles(request))
                .isInstanceOf(PhantomBusterException.class)
                .hasMessageContaining("Agent did not complete");
    }
}
