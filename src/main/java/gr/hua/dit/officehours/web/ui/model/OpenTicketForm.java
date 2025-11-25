package gr.hua.dit.officehours.web.ui.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OpenTicketForm(
    @NotNull @Positive Long teacherId,
    @NotNull @NotBlank @Size(max = 255) String subject,
    @NotNull @NotBlank @Size(max = 1000) String studentContent
) {}
