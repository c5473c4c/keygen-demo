package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MachineFingerprint {
    private static final Logger logger = LoggerFactory.getLogger(MachineFingerprint.class);

    public static String generate() {
        try {
            String osName = System.getProperty("os.name", "unknown");
            String osArch = System.getProperty("os.arch", "unknown");
            String userName = System.getProperty("user.name", "unknown");
            String javaVendor = System.getProperty("java.vendor", "unknown");
            String homeDir = System.getProperty("user.home", "unknown");

            // Get MAC address of primary network interface
            String macAddress = getPrimaryMacAddress();

            // Get hostname
            String hostname = InetAddress.getLocalHost().getHostName();

            // Combine all identifying information
            String combined = String.format("%s|%s|%s|%s|%s|%s|%s",
                    osName, osArch, userName, javaVendor, homeDir, macAddress, hostname);

            // Generate SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes());

            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Return first 16 characters for a shorter fingerprint
            return hexString.substring(0, 16).toUpperCase();

        } catch (Exception e) {
            // Fallback to a simpler fingerprint if there's an error
            return generateFallbackFingerprint();
        }
    }

    private static String getPrimaryMacAddress() {
        try {
            List<String> macAddresses = new ArrayList<>();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();
                String name = network.getName().toLowerCase();

                // Skip loopback, virtual, and unstable macOS interfaces
                if (network.isLoopback() || network.isVirtual() || 
                    name.startsWith("utun") || // VPN tunnels
                    name.equals("awdl0") ||     // Apple Wireless Direct Link (AirDrop)
                    name.equals("llw0") ||      // Low Latency WLAN
                    name.startsWith("bridge") || // Bridge interfaces
                    network.isPointToPoint()) {  // Point-to-point interfaces
                    continue;
                }

                byte[] mac = network.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X", mac[i]));
                        if (i < mac.length - 1) sb.append(":");
                    }
                    macAddresses.add(sb.toString());
                }
            }

            // Sort MAC addresses to ensure consistent ordering
            if (!macAddresses.isEmpty()) {
                Collections.sort(macAddresses);
                // Return the first (lexicographically smallest) MAC address
                return macAddresses.get(0);
            }
        } catch (Exception e) {
            // Ignore and continue to fallback
        }
        return "NO-MAC";
    }

    private static String generateFallbackFingerprint() {
        try {
            String fallback = System.getProperty("os.name", "unknown") +
                    System.getProperty("user.name", "unknown") +
                    System.getProperty("user.home", "unknown");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(fallback.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < 8 && i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            // Last resort - return a constant
            return "FALLBACK-FP";
        }
    }

    public static void main(String[] args) {
        // Test the fingerprint generation
        logger.info("Machine Fingerprint: {}", generate());
    }
}