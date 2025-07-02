package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.LicenseValidationResponse;
import org.example.model.MachineActivationResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class KeygenLicenseValidator {
    private static final Logger logger = LoggerFactory.getLogger(KeygenLicenseValidator.class);
    private final KeygenConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public KeygenLicenseValidator(KeygenConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public LicenseValidationResponse validateLicense(String licenseKey) throws IOException, InterruptedException {
        return validateLicense(licenseKey, null);
    }

    public LicenseValidationResponse validateLicense(String licenseKey, ValidationScope scope) throws IOException, InterruptedException {
        JSONObject requestBody = new JSONObject();
        JSONObject meta = new JSONObject();
        meta.put("key", licenseKey);

        if (scope != null) {
            JSONObject scopeJson = new JSONObject();
            if (scope.getFingerprint() != null) {
                scopeJson.put("fingerprint", scope.getFingerprint());
            }
            if (scope.getMachine() != null) {
                scopeJson.put("machine", scope.getMachine());
            }
            if (scope.getProduct() != null) {
                scopeJson.put("product", scope.getProduct());
            }
            meta.put("scope", scopeJson);
        }

        requestBody.put("meta", meta);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.getLicenseValidationUrl()))
                .header("Content-Type", "application/vnd.api+json")
                .header("Accept", "application/vnd.api+json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return objectMapper.readValue(response.body(), LicenseValidationResponse.class);
        } else {
            throw new IOException("License validation failed with status code: " + response.statusCode() +
                    "\nResponse: " + response.body());
        }
    }

    public MachineActivationResponse activateMachine(String licenseKey, String fingerprint) throws IOException, InterruptedException {
        return activateMachine(licenseKey, fingerprint, null);
    }

    public MachineActivationResponse activateMachine(String licenseKey, String fingerprint, String machineName) throws IOException, InterruptedException {
        // Get the license ID first
        LicenseValidationResponse validationResponse = validateLicense(licenseKey);
        if (!validationResponse.getMeta().isValid() || validationResponse.getData() == null) {
            throw new IOException("Cannot activate machine: License validation failed");
        }

        String licenseId = validationResponse.getData().getId();
        return activateMachineWithLicenseId(licenseKey, licenseId, fingerprint, machineName);
    }

    public MachineActivationResponse activateMachineWithLicenseId(String licenseKey, String licenseId, String fingerprint, String machineName) throws IOException, InterruptedException {
        JSONObject requestBody = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject attributes = new JSONObject();
        JSONObject relationships = new JSONObject();
        JSONObject license = new JSONObject();
        JSONObject licenseData = new JSONObject();

        // Set the fingerprint
        attributes.put("fingerprint", fingerprint);

        // Add machine name if provided
        if (machineName != null && !machineName.trim().isEmpty()) {
            attributes.put("name", machineName.trim());
        }

        // Add system information
        try {
            attributes.put("platform", System.getProperty("os.name"));
            attributes.put("hostname", java.net.InetAddress.getLocalHost().getHostName());
            attributes.put("cores", Runtime.getRuntime().availableProcessors());
        } catch (Exception e) {
            // Continue without system info if unavailable
        }

        // Set up license relationship using license UUID (not the key)
        licenseData.put("type", "licenses");
        licenseData.put("id", licenseId);  // Use the UUID, not the key
        license.put("data", licenseData);
        relationships.put("license", license);

        data.put("type", "machines");
        data.put("attributes", attributes);
        data.put("relationships", relationships);
        requestBody.put("data", data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.getMachineActivationUrl()))
                .header("Content-Type", "application/vnd.api+json")
                .header("Accept", "application/vnd.api+json")
                .header("Authorization", "License " + licenseKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return objectMapper.readValue(response.body(), MachineActivationResponse.class);
        } else {
            throw new IOException("Machine activation failed with status code: " + response.statusCode() +
                    "\nResponse: " + response.body());
        }
    }

    public LicenseValidationResponse validateAndActivateIfNeeded(String licenseKey, String fingerprint) throws IOException, InterruptedException {
        return validateAndActivateIfNeeded(licenseKey, fingerprint, null);
    }

    public LicenseValidationResponse validateAndActivateIfNeeded(String licenseKey, String fingerprint, String machineName) throws IOException, InterruptedException {
        // First, try to validate with fingerprint
        ValidationScope scope = ValidationScope.withFingerprint(fingerprint);
        LicenseValidationResponse response = validateLicense(licenseKey, scope);

        // If validation failed due to NO_MACHINES or FINGERPRINT_SCOPE_MISMATCH (fingerprint not activated)
        if (!response.getMeta().isValid() &&
                ("NO_MACHINES".equals(response.getMeta().getCode()) ||
                        "FINGERPRINT_SCOPE_MISMATCH".equals(response.getMeta().getCode()))) {
            if ("NO_MACHINES".equals(response.getMeta().getCode())) {
                logger.info("No machines activated for this license. Attempting to activate this machine...");
            } else {
                logger.info("Machine fingerprint not found among activated machines. Attempting to activate this machine...");
            }

            try {
                // First, get the license ID by validating without fingerprint
                LicenseValidationResponse basicResponse = validateLicense(licenseKey);

                if (basicResponse.getMeta().isValid() && basicResponse.getData() != null) {
                    String licenseId = basicResponse.getData().getId();
                    logger.debug("Using license ID: {}", licenseId);

                    // Attempt to activate the machine with the correct license ID
                    MachineActivationResponse activationResponse = activateMachineWithLicenseId(licenseKey, licenseId, fingerprint, machineName);
                    logger.info("Machine activated successfully! ID: {}", activationResponse.getData().getId());

                    // Validate again after activation
                    response = validateLicense(licenseKey, scope);
                } else {
                    logger.error("Could not retrieve license ID for activation. License validation failed.");
                }

            } catch (IOException e) {
                logger.error("Failed to activate machine: {}", e.getMessage(), e);
                // Return the original validation response
            }
        }

        return response;
    }

    public static class ValidationScope {
        private String fingerprint;
        private String machine;
        private String product;

        public ValidationScope() {
        }

        public ValidationScope(String fingerprint) {
            this.fingerprint = fingerprint;
        }

        public static ValidationScope withFingerprint(String fingerprint) {
            return new ValidationScope(fingerprint);
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }

        public String getMachine() {
            return machine;
        }

        public void setMachine(String machine) {
            this.machine = machine;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }
    }
}