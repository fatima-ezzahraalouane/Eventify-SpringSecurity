package com.eventify.springsecurity.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateDTO {

    @NotBlank(message = "Le titre est requis")
    private String title;

    private String description;

    private String location;

    @NotNull(message = "La date et l'heure sont requises")
    @Future(message = "La date de l'événement doit être dans le futur")
    private LocalDateTime dateTime;

    @NotNull(message = "La capacité est requise")
    @Min(value = 1, message = "La capacité doit être d'au moins 1")
    private Integer capacity;
}

