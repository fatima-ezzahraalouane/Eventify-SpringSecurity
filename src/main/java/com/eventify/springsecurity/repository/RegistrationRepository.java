package com.eventify.springsecurity.repository;

import com.eventify.springsecurity.entity.Registration;
import com.eventify.springsecurity.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    
}

