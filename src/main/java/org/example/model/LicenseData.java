package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a license data object in the Keygen.sh API response.
 * This class follows the JSON:API specification with id, type, and attributes fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseData {
    /** Unique identifier for the license */
    @JsonProperty("id")
    private String id;

    /** Resource type (always "licenses" for license objects) */
    @JsonProperty("type")
    private String type;

    /** License attributes containing detailed license information */
    @JsonProperty("attributes")
    private LicenseAttributes attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LicenseAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(LicenseAttributes attributes) {
        this.attributes = attributes;
    }
}