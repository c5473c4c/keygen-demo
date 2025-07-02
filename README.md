# Keygen.sh License Validation Demo

A Java demo application that authenticates and validates license keys using the Keygen.sh API.

## Prerequisites

- Java 21 or later
- Maven 3.x
- A Keygen.sh account (get one at https://keygen.sh)

## Building the Application

```bash
mvn clean compile
```

## Running the Application

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Or compile and run directly:

```bash
mvn clean compile
java -cp target/classes org.example.Main
```

## Usage

When you run the application, you'll be prompted for:

1. **Account ID**: Your Keygen.sh account ID (found in your Keygen dashboard)
2. **License Key**: The license key you want to validate
3. **Machine Fingerprint**: Automatically generated based on system properties, or you can provide a custom one

### Example Session

```
=== Keygen.sh License Validation Demo ===

Enter your Keygen.sh Account ID: your-account-id
Enter the license key to validate: DEMO-LICKEY-12345

Auto-generated machine fingerprint: 7B3A5F9C2E8D1A6F
Use auto-generated fingerprint? (Y/n): y
Using auto-generated fingerprint.

Validating license key...
Validating with fingerprint: 7B3A5F9C2E8D1A6F
Machine fingerprint not activated. Attempting to activate...
Machine activated successfully! ID: machine_abc123

=== Validation Result ===
Valid: true
Detail: The license is valid
Code: VALID

=== License Details ===
License ID: abc123
Status: ACTIVE
Scheme: ED25519_SIGN
Uses: 0
Suspended: false
```

## Features

- License key validation via Keygen.sh API
- Automatic machine fingerprint generation based on system properties
- **Automatic machine activation** when fingerprint is not yet activated
- Optional custom machine fingerprint support
- Detailed validation response display
- Error handling for network and API errors

## Project Structure

```
keygen-demo/
├── src/main/java/org/example/
│   ├── Main.java                    # Main application entry point
│   ├── KeygenConfig.java            # API configuration
│   ├── KeygenLicenseValidator.java  # License validation logic
│   ├── MachineFingerprint.java      # Machine fingerprint generation
│   └── model/                       # Response model classes
│       ├── LicenseValidationResponse.java
│       ├── LicenseData.java
│       ├── LicenseAttributes.java
│       └── ValidationMeta.java
└── pom.xml                          # Maven configuration
```

## API Documentation

For more information about Keygen.sh API:
- [API Reference](https://keygen.sh/docs/api/)
- [License Validation](https://keygen.sh/docs/api/licenses/)
- [Authentication](https://keygen.sh/docs/api/authentication/)

## Machine Fingerprint Generation

The application automatically generates a unique machine fingerprint based on:
- Operating system name and architecture
- User name and home directory
- Primary network interface MAC address
- System hostname
- Java vendor information

The fingerprint is generated as a SHA-256 hash of these combined properties, ensuring uniqueness across different machines while remaining consistent on the same machine.

## Automatic Machine Activation

When a license validation fails with the error "fingerprint is not activated (has no associated machines)" and error code `NO_MACHINES`, the application automatically attempts to activate the machine fingerprint:

1. **Detection**: Recognizes `NO_MACHINES` error code in validation response
2. **Activation**: Sends a machine activation request using the license key
3. **Retry**: Re-validates the license after successful activation
4. **Reporting**: Displays activation status and machine ID

This feature eliminates the manual step of machine activation, providing a seamless user experience for new machines while respecting license limits.

## Security Notes

- Never commit your account ID or license keys to version control
- The license validation endpoint doesn't require authentication
- Machine fingerprints help enforce license restrictions per device
- For production use, consider implementing proper error handling and logging
- Store sensitive configuration in environment variables or secure configuration files