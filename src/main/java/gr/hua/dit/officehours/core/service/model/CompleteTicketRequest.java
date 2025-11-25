package gr.hua.dit.officehours.core.service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CompleteTicketRequest(
    @NotNull @Positive Long id,
    @NotNull @NotBlank @Size(max = 1000) String teacherContent
) {}
