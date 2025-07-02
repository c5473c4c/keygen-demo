# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Common Development Commands

### Build and Run
- `mvn clean compile` - Clean and compile the project
- `mvn test` - Run tests (no tests currently implemented)
- `mvn package` - Create JAR file
- `mvn exec:java -Dexec.mainClass="org.example.Main"` - Run the main application
- `java -cp target/classes org.example.Main` - Run compiled application directly

### Development
- `mvn clean` - Clean build artifacts
- `mvn install` - Install to local Maven repository

## High-Level Architecture

This is a Maven-based Java 23 application that demonstrates Keygen.sh license validation. The architecture follows a simple layered approach:

### Core Components
1. **Main.java** - Interactive CLI entry point that prompts for account ID, license key, and uses auto-generated machine fingerprint
2. **KeygenConfig.java** - Configuration holder for Keygen.sh API endpoints and account ID
3. **KeygenLicenseValidator.java** - HTTP client implementation for license validation and machine activation
4. **MachineFingerprint.java** - Generates unique machine fingerprints based on system properties
5. **model/** - Jackson-annotated POJOs for JSON serialization/deserialization of API responses

### Key Design Decisions
- Uses Java 23's built-in HttpClient for API requests
- Jackson for JSON processing (primary), with org.json as secondary dependency
- Automatic machine fingerprint generation using system properties (OS, MAC address, etc.)
- Auto-activation of machines when license validation fails with NO_MACHINES error
- License key authentication for machine activation requests

### API Integration Pattern
The application follows an enhanced Keygen.sh validation flow:
1. Generate unique machine fingerprint automatically
2. POST to `/v1/accounts/{accountId}/licenses/actions/validate-key` with fingerprint scope
3. If validation fails with NO_MACHINES error, automatically POST to `/v1/accounts/{accountId}/machines` to activate
4. Re-validate license after successful activation
5. Display validation details and license attributes