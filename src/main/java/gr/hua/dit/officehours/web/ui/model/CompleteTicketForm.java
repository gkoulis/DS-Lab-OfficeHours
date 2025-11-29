package gr.hua.dit.officehours.web.ui.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompleteTicketForm(
    @NotNull @NotBlank @Size(max = 1000) String teacherContent
) {}
