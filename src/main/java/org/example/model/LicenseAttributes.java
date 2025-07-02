package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the attributes of a license in the Keygen.sh API response.
 * This class contains detailed information about a license including its status,
 * usage limits, expiration, and other metadata.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicenseAttributes {
    /** The license key string */
    @JsonProperty("key")
    private String key;

    /** Current status of the license (e.g., "ACTIVE", "EXPIRED", "SUSPENDED") */
    @JsonProperty("status")
    private String status;

    /** Current number of times the license has been used/activated */
    @JsonProperty("uses")
    private Integer uses;

    /** Maximum number of uses/activations allowed for this license (null = unlimited) */
    @JsonProperty("maxUses")
    private Integer maxUses;

    /** ISO 8601 timestamp when the license expires (null = never expires) */
    @JsonProperty("expiry")
    private String expiry;

    /** Whether the license is currently suspended */
    @JsonProperty("suspended")
    private Boolean suspended;

    /** License key scheme identifier used to generate this license */
    @JsonProperty("scheme")
    private String scheme;

    /** Whether the license key is encrypted */
    @JsonProperty("encrypted")
    private Boolean encrypted;

    /** ISO 8601 timestamp when the license was created */
    @JsonProperty("createdAt")
    private String createdAt;

    /** ISO 8601 timestamp when the license was last updated */
    @JsonProperty("updatedAt")
    private String updatedAt;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUses() {
        return uses;
    }

    public void setUses(Integer uses) {
        this.uses = uses;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}