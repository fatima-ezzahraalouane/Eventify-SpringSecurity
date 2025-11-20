package com.eventify.springsecurity.repository;

import com.eventify.springsecurity.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Trouve tous les événements organisés par un utilisateur
     * @param organizerId ID de l'organisateur
     * @return Liste des événements organisés
     */
    List<Event> findByOrganizerId(Long organizerId);

    
}

