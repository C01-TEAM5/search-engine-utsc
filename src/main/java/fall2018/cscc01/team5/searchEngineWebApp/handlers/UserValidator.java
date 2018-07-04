package fall2018.cscc01.team5.searchEngineWebApp.handlers;

import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

public class UserValidator {
	

	public UserValidator () {
		
	}

	/**
	 * Generate a random salt, a random data that append to password before
	 * obtaining hash.
	 * 
	 * @return a random salt
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getSalt() {
		//TODO
	    return null;
	}

	
	/**
	 * Generate a secure password/hash.
	 * 
	 * @param password the original password to hash
	 * @return a secure password/hash
	 */
	public static String get_PBKDF2_SecurePassword(String password) {
		//TODO
		return null;
	}

	// Helper function in get_PBKDF2_SecurePassword(String password)
	private String toHex(byte[] salt) {
		//TODO
		return null;
	}
	
	/**
	 * Validate the input password is correct.
	 * 
	 * @param originalPassword    the input password need to validate
	 * @param storedPassword   the hash password in database
	 * @return  true if originalPassword is the same as storedPassword
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
    public static boolean validatePassword(String originalPassword, String storedPassword) {
    	//TODO
    	return true;
    }
   
        
}
