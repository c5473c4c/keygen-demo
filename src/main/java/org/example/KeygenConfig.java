package org.example;

public class KeygenConfig {
    private final String accountId;
    private final String apiUrl;

    public KeygenConfig(String accountId) {
        this.accountId = accountId;
        this.apiUrl = "https://api.keygen.sh/v1/accounts/" + accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getLicenseValidationUrl() {
        return apiUrl + "/licenses/actions/validate-key";
    }

    public String getMachineActivationUrl() {
        return apiUrl + "/machines";
    }
}