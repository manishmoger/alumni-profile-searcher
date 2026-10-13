package com.alumni.search;

import com.alumni.search.dto.SearchRequest;
import com.alumni.search.model.AlumniProfile;
import com.alumni.search.repository.AlumniProfileRepository;
import com.alumni.search.service.AlumniService;
import com.alumni.search.service.PhantomBusterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumniServiceTest {

    @Mock
    private PhantomBusterService phantomBusterService;

    @Mock
    private AlumniProfileRepository repository;

    @InjectMocks
    private AlumniService alumniService;

    private SearchRequest request;
    private List<AlumniProfile> mockProfiles;

    @BeforeEach
    void setUp() {
        request = new SearchRequest();
        request.setUniversity("IIT Bombay");
        request.setDesignation("Software Engineer");
        request.setPassoutYear("2020");

        mockProfiles = List.of(
                new AlumniProfile("Rahul Mehta", "Software Engineer", "IIT Bombay", "Bangalore, India", "Software Engineer at Google", 2020),
                new AlumniProfile("Priya Sharma", "Data Scientist", "IIT Bombay", "Hyderabad, India", "Data Scientist at Microsoft", 2019)
        );
    }

    @Test
    void searchAndSave_shouldFetchAndPersistProfiles() {
        when(phantomBusterService.fetchAlumniProfiles(request)).thenReturn(mockProfiles);
        when(repository.saveAll(mockProfiles)).thenReturn(mockProfiles);

        List<AlumniProfile> result = alumniService.searchAndSave(request);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Rahul Mehta");
        verify(repository).saveAll(mockProfiles);
    }

    @Test
    void getAllProfiles_shouldReturnAllSavedProfiles() {
        when(repository.findAll()).thenReturn(mockProfiles);

        List<AlumniProfile> result = alumniService.getAllProfiles();

        assertThat(result).hasSize(2);
        verify(repository).findAll();
    }

    @Test
    void searchAndSave_withNoResults_shouldSaveEmptyList() {
        when(phantomBusterService.fetchAlumniProfiles(request)).thenReturn(List.of());
        when(repository.saveAll(List.of())).thenReturn(List.of());

        List<AlumniProfile> result = alumniService.searchAndSave(request);

        assertThat(result).isEmpty();
    }
}
