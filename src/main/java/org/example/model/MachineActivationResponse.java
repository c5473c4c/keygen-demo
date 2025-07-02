package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response from the Keygen.sh machine activation endpoint.
 * Contains the newly created machine data following JSON:API format.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineActivationResponse {
    /** The machine data containing details of the activated machine */
    @JsonProperty("data")
    private MachineData data;

    /** Optional metadata about the activation (may be null or empty) */
    @JsonProperty("meta")
    private Object meta;

    public MachineData getData() {
        return data;
    }

    public void setData(MachineData data) {
        this.data = data;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }
}