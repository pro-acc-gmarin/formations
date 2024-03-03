package utils.filter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EtagUtils {

    public static String generateETag(final String data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final  byte[] hash = digest.digest(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate ETag", e);
        }
    }
}
