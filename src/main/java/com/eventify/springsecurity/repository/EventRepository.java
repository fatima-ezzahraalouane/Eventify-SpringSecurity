package com.eventify.springsecurity.repository;

import com.eventify.springsecurity.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganizerId(Long organizerId);

    boolean existsByIdAndOrganizerId(Long eventId, Long organizerId);
}

