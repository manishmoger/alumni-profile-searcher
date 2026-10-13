package com.alumni.search.service;

import com.alumni.search.dto.SearchRequest;
import com.alumni.search.model.AlumniProfile;
import com.alumni.search.repository.AlumniProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumniService {

    private final PhantomBusterService phantomBusterService;
    private final AlumniProfileRepository repository;

    public AlumniService(PhantomBusterService phantomBusterService, AlumniProfileRepository repository) {
        this.phantomBusterService = phantomBusterService;
        this.repository = repository;
    }

    public List<AlumniProfile> searchAndSave(SearchRequest request) {
        List<AlumniProfile> profiles = phantomBusterService.fetchAlumniProfiles(request);
        return repository.saveAll(profiles);
    }

    public List<AlumniProfile> getAllProfiles() {
        return repository.findAll();
    }
}
