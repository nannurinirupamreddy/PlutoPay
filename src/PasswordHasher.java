import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Salted, slow hashing for passwords and PINs (PBKDF2 with HMAC-SHA256).
 *
 * Stored format: pbkdf2$<iterations>$<salt base64>$<hash base64>
 * Each secret gets its own random salt, so two users with the same
 * password end up with different hashes.
 */
public final class PasswordHasher {

    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    public static String hash(String secret) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] key = derive(secret, salt, ITERATIONS);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIX + "$" + ITERATIONS + "$" + b64.encodeToString(salt) + "$" + b64.encodeToString(key);
    }

    /** True if the secret matches a stored hash. Plain-text values saved by older versions still verify. */
    public static boolean verify(String secret, String stored) {
        if (secret == null || stored == null || stored.isEmpty()) {
            return false;
        }
        if (!isHashed(stored)) {
            return MessageDigest.isEqual(secret.getBytes(), stored.getBytes());
        }
        String[] parts = stored.split("\\$");
        int iterations = Integer.parseInt(parts[1]);
        Base64.Decoder b64 = Base64.getDecoder();
        byte[] salt = b64.decode(parts[2]);
        byte[] expected = b64.decode(parts[3]);
        // Constant-time comparison so timing doesn't leak how much of the hash matched.
        return MessageDigest.isEqual(derive(secret, salt, iterations), expected);
    }

    public static boolean isHashed(String stored) {
        return stored != null && stored.startsWith(PREFIX + "$");
    }

    private static byte[] derive(String secret, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(secret.toCharArray(), salt, iterations, KEY_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("PBKDF2 is not available", e);
        } finally {
            spec.clearPassword();
        }
    }
}
