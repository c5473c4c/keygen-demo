package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents validation metadata in the Keygen.sh API response.
 * Contains the validation result status, human-readable details,
 * and machine-readable error codes for failed validations.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationMeta {
    /** Whether the license validation was successful */
    @JsonProperty("valid")
    private boolean valid;

    /** Human-readable description of the validation result */
    @JsonProperty("detail")
    private String detail;

    /** Machine-readable error code (e.g., "NO_MACHINES", "EXPIRED", "SUSPENDED") */
    @JsonProperty("code")
    private String code;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}