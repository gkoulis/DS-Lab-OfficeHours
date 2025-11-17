package gr.hua.dit.officehours.core.port.impl.dto;

/**
 * PhoneNumberValidationResult DTO.
 */
public record PhoneNumberValidationResult(
        String raw,
        boolean valid,
        String type,
        String e164
) {

    public boolean isValid() {
        return this.valid;
    }

    public boolean isValidMobile() {
        if (!this.valid) return false;
        if (this.type == null) return false;
        return "mobile".equals(this.type);
    }
}
