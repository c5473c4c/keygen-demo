package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response from the Keygen.sh license validation endpoint.
 * Contains the license data and validation metadata following JSON:API format.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseValidationResponse {
    /** The license data containing license details */
    @JsonProperty("data")
    private LicenseData data;

    /** Metadata about the validation result including status and error codes */
    @JsonProperty("meta")
    private ValidationMeta meta;

    public LicenseData getData() {
        return data;
    }

    public void setData(LicenseData data) {
        this.data = data;
    }

    public ValidationMeta getMeta() {
        return meta;
    }

    public void setMeta(ValidationMeta meta) {
        this.meta = meta;
    }
}