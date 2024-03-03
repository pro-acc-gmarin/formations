package utils.cache;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EtagUtils {

    public static String generateETag(final Object data) {
        try {
            // Utilisez SHA-256 pour le hash
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convertissez vos données en chaîne de caractères qui représente leur état
            // Par exemple, pour un objet utilisateur, cela pourrait être une concaténation de nom, email, et la date de dernière modification
            final String inputString = data.toString();

            // Appliquez le hash SHA-256 à cette chaîne
            final byte[] hash = digest.digest(inputString.getBytes(StandardCharsets.UTF_8));

            // Convertissez le tableau de bytes en chaîne hexadécimale
            final StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Impossible de calculer le hash SHA-256", e);
        }
    }
}
