package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the attributes of a machine in the Keygen.sh API response.
 * Contains information about the activated machine including its fingerprint,
 * hostname, platform details, and timestamps.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineAttributes {
    /** Unique fingerprint identifying this machine */
    @JsonProperty("fingerprint")
    private String fingerprint;

    /** Optional friendly name for the machine */
    @JsonProperty("name")
    private String name;

    /** Machine's hostname */
    @JsonProperty("hostname")
    private String hostname;

    /** Platform/OS information (e.g., "darwin", "linux", "windows") */
    @JsonProperty("platform")
    private String platform;

    /** IP address of the machine (if available) */
    @JsonProperty("ip")
    private String ip;

    /** Number of CPU cores on the machine */
    @JsonProperty("cores")
    private Integer cores;

    /** ISO 8601 timestamp when the machine was activated */
    @JsonProperty("createdAt")
    private String createdAt;

    /** ISO 8601 timestamp when the machine was last updated */
    @JsonProperty("updatedAt")
    private String updatedAt;

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCores() {
        return cores;
    }

    public void setCores(Integer cores) {
        this.cores = cores;
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