package com.eventify.springsecurity.repository;

import com.eventify.springsecurity.entity.Registration;
import com.eventify.springsecurity.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /**
     * Trouve toutes les inscriptions d'un utilisateur
     * @param userId ID de l'utilisateur
     * @return Liste des inscriptions
     */
    List<Registration> findByUserId(Long userId);

    /**
     * Trouve toutes les inscriptions d'un événement
     * @param eventId ID de l'événement
     * @return Liste des inscriptions
     */
    List<Registration> findByEventId(Long eventId);

    /**
     * Trouve une inscription spécifique par utilisateur et événement
     * @param userId ID de l'utilisateur
     * @param eventId ID de l'événement
     * @return Inscription trouvée
     */
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

    /**
     * Vérifie si un utilisateur est déjà inscrit à un événement
     * @param userId ID de l'utilisateur
     * @param eventId ID de l'événement
     * @return true si l'inscription existe
     */
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    
}

