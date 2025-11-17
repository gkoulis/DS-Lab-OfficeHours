package gr.hua.dit.officehours.core.port.impl.dto;

import gr.hua.dit.officehours.core.model.PersonType;

/**
 * LookupResult DTO.
 */
public record LookupResult(
    String raw,
    boolean exists,
    String huaId,
    PersonType type
) {}
