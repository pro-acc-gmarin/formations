package utils.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.infrastructure.dao.UserDao;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

public class PasswordHelper {
    private static final Random RANDOM = new SecureRandom();

    private static final Logger LOGGER = LogManager.getLogger(PasswordHelper.class);

    public static class GENERATOR {
        private static final char[] ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]?".toCharArray();
        private static final int PASSWORD_LENGTH = 10;

        public static String generatePassword() {
            char[] password = new char[PASSWORD_LENGTH];
            password[0] = ALLOWED_CHARS[RANDOM.nextInt(ALLOWED_CHARS.length)];
            for (int i = 1; i < PASSWORD_LENGTH; ++i) {
                password[i] = ALLOWED_CHARS[RANDOM.nextInt(ALLOWED_CHARS.length)];
            }
            shuffleWithFisherYatesAlgorithm(password);
            return new String(password);
        }

        private static void shuffleWithFisherYatesAlgorithm(char[] password){
            for (int i = PASSWORD_LENGTH - 1; i > 0; --i) {
                int j = RANDOM.nextInt(i + 1);
                char temp = password[i];
                password[i] = password[j];
                password[j] = temp;
            }
        }

    }


    public static class HASH {
        private static final int ITERATIONS = 65536;
        private static final int KEY_LENGTH = 50;
        private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
        private static final int SALT_LENGTH = 16;

        public static String generateSalt() {
            byte[] salt = new byte[SALT_LENGTH];
            RANDOM.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        }


        public static Optional<String> hashPassword(String password, String salt) {

            // Convert the password to a char array, allowing GC to clean the unused string.
            char[] chars = password.toCharArray();
            byte[] bytes = salt.getBytes();

            PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

            // Clean the char array after using.
            Arrays.fill(chars, Character.MIN_VALUE);

            try {
                SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
                byte[] securePassword = fac.generateSecret(spec).getEncoded();
                return Optional.of(Base64.getEncoder().encodeToString(securePassword));

            } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
                LoggerHelper.logError(LOGGER, LoggerHelper.SECURITY, exception);
                return Optional.empty();
            } finally {
                spec.clearPassword();
            }
        }

        public static boolean verifyPassword(String password, String key, String salt) {
            Optional<String> optEncrypted = hashPassword(password, salt);
            if (!optEncrypted.isPresent()) return false;
            return optEncrypted.get().equals(key);
        }
    }
}
