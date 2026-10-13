package com.alumni.search.repository;

import com.alumni.search.model.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, Long> {
}
