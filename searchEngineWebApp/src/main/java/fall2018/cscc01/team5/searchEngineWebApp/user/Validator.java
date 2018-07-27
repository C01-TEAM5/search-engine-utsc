package fall2018.cscc01.team5.searchEngineWebApp.user;

import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;


/**
 * Class to validate user passwords to determine if the user can log in. Learning about PBKDF2 algorithm and
 * implementing validation was found here: https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 */
public class Validator {

    private static final int HASH_ITERATIONS = 100;

    /**
     * Generate a random salt, a random data that append to password before obtaining hash.
     *
     * @return a random salt
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSalt () {

        SecureRandom randomizer = new SecureRandom();
        byte[] newSalt = new byte[16];
        randomizer.nextBytes(newSalt);
        return newSalt;
    }


    /**
     * Generate a secure password/hash.
     *
     * @param password the original password to hash
     * @return a secure password/hash
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String getSaltedHash (String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        char[] passChar = password.toCharArray();

        //SHA-256 algorithm generates 256 bits
        PBEKeySpec pbe = new PBEKeySpec(passChar, salt, HASH_ITERATIONS, 256);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] saltedHash = keyFactory.generateSecret(pbe).getEncoded();

        return Hex.encodeHexString(salt) + ":" + Hex.encodeHexString(saltedHash);
    }

    /**
     * Validate the input password is correct.
     *
     * @param enteredHash the input password need to validate
     * @param storedHash  the hash password in database
     * @return true if originalPassword is the same as storedPassword
     * @throws DecoderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean validateHash (String enteredHash, String storedHash) throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {

        String[] seperatedPassword = storedHash.split(":");
        byte[] salt = Hex.decodeHex(seperatedPassword[0]);
        String hashedEnteredPassword = getSaltedHash(enteredHash, salt);

        return hashedEnteredPassword.equals(storedHash);
    }

    /**
     * Encrypt a given message
     * @param toEncrypt
     * @return
     */
    public static String simpleEncrypt(String toEncrypt) {

        String res = Base64.encodeBase64String((toEncrypt + "SUPERDUPERSECTRET").getBytes());

        return  res;
    }

    /**
     * Decde a simple encryption
     *
     * @param hash - the hash to decode
     * @return the decoded hash.
     * @throws DecoderException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static String simpleDecode (String hash) {
        String res = new String(Base64.decodeBase64(hash.getBytes())).split("SUPERDUPERSECTRET")[0];

        return res;
    }


}
