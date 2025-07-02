package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a machine data object in the Keygen.sh API response.
 * This class follows the JSON:API specification with id, type, and attributes fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineData {
    /** Unique identifier for the machine */
    @JsonProperty("id")
    private String id;

    /** Resource type (always "machines" for machine objects) */
    @JsonProperty("type")
    private String type;

    /** Machine attributes containing detailed machine information */
    @JsonProperty("attributes")
    private MachineAttributes attributes;

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

    public MachineAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(MachineAttributes attributes) {
        this.attributes = attributes;
    }
}