package org.example;

import org.example.model.LicenseValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("=== Keygen.sh License Validation Demo ===");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your Keygen.sh Account ID: "); // Keep console prompt
        String accountId = scanner.nextLine().trim();

        if (accountId.isEmpty()) {
            logger.error("Error: Account ID is required!");
            System.exit(1);
        }

        KeygenConfig config = new KeygenConfig(accountId);
        KeygenLicenseValidator validator = new KeygenLicenseValidator(config);

        System.out.print("Enter the license key to validate: "); // Keep console prompt
        String licenseKey = scanner.nextLine().trim();

        if (licenseKey.isEmpty()) {
            logger.error("Error: License key is required!");
            System.exit(1);
        }

        // Generate machine fingerprint automatically
        String autoFingerprint = MachineFingerprint.generate();
        logger.info("Auto-generated machine fingerprint: {}", autoFingerprint);

        System.out.print("\nUse auto-generated fingerprint? (Y/n): "); // Keep console prompt
        String useAuto = scanner.nextLine().trim();

        String fingerprint;
        if (useAuto.isEmpty() || useAuto.toLowerCase().startsWith("y")) {
            fingerprint = autoFingerprint;
            logger.info("Using auto-generated fingerprint.");
        } else {
            System.out.print("Enter custom machine fingerprint (optional, press Enter to skip): "); // Keep console prompt
            fingerprint = scanner.nextLine().trim();
        }

        logger.info("Validating license key...");

        try {
            LicenseValidationResponse response;

            if (!fingerprint.isEmpty()) {
                logger.debug("Validating with fingerprint: {}", fingerprint);
                // Use the new auto-activation method
                response = validator.validateAndActivateIfNeeded(licenseKey, fingerprint, "Demo Machine");
            } else {
                response = validator.validateLicense(licenseKey);
            }

            logger.info("=== Validation Result ===");
            logger.info("Valid: {}", response.getMeta().isValid());
            logger.info("Detail: {}", response.getMeta().getDetail());

            if (response.getMeta().getCode() != null) {
                logger.info("Code: {}", response.getMeta().getCode());
            }

            if (response.getMeta().isValid() && response.getData() != null) {
                logger.info("=== License Details ===");
                logger.info("License ID: {}", response.getData().getId());

                if (response.getData().getAttributes() != null) {
                    var attributes = response.getData().getAttributes();
                    logger.info("Status: {}", attributes.getStatus());
                    logger.info("Scheme: {}", attributes.getScheme());

                    if (attributes.getUses() != null) {
                        logger.info("Uses: {}{}", attributes.getUses(),
                                (attributes.getMaxUses() != null ? "/" + attributes.getMaxUses() : ""));
                    }

                    if (attributes.getExpiry() != null) {
                        logger.info("Expiry: {}", attributes.getExpiry());
                    }

                    logger.info("Suspended: {}",
                            (attributes.getSuspended() != null ? attributes.getSuspended() : false));
                }
            } else if (!response.getMeta().isValid()) {
                logger.error("License validation failed!");
            }

        } catch (IOException e) {
            logger.error("Error validating license: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        } finally {
            scanner.close();
        }
    }
}