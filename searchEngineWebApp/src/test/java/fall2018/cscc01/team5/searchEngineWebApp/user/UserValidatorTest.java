package fall2018.cscc01.team5.searchEngineWebApp.user;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class UserValidatorTest {

    /* Test valid password entry
     * Tests include with characters, numbers, symbols
     */
    @Test
    public void testValidHash() throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String salt = "123456789ABCDEF123456789ABCDEF";
        byte[] testSalt = Hex.decodeHex(salt.toCharArray());     
        String password = "password";
        String hashedPassword = Validator.getSaltedHash(password, testSalt);
        assertEquals(true,Validator.validateHash("password", hashedPassword));
        
        salt = "123456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "test123";
        hashedPassword = Validator.getSaltedHash(password, testSalt);
        assertEquals(true,Validator.validateHash("test123", hashedPassword));
        
        salt = "AAA456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "!tEst";
        hashedPassword = Validator.getSaltedHash(password, testSalt);
        assertEquals(true,Validator.validateHash("!tEst", hashedPassword));
    }
    
    /* Test invalid passwords
     */
    @Test
    public void testInvalidHash() throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String salt = "123456789ABCDEF123456789ABCDEF";
        byte[] testSalt = Hex.decodeHex(salt.toCharArray());     
        String password = "password";
        String hashedPassword = Validator.getSaltedHash(password, testSalt);
        assertEquals(false,Validator.validateHash("passwor", hashedPassword));
        assertEquals(false,Validator.validateHash("pass", hashedPassword));
        assertEquals(false,Validator.validateHash("hellothe", hashedPassword));
        
        salt = "123456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "test123";
        hashedPassword = Validator.getSaltedHash(password, testSalt);
        assertEquals(false,Validator.validateHash("test1234", hashedPassword));
        assertEquals(false,Validator.validateHash("test", hashedPassword));
        assertEquals(false,Validator.validateHash("TEST123", hashedPassword));
        assertEquals(false,Validator.validateHash("t!st123", hashedPassword));
        
        salt = "AAA456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "!tEst";
        hashedPassword = Validator.getSaltedHash(password, testSalt);
        assertEquals(false,Validator.validateHash("", hashedPassword));
        assertEquals(false,Validator.validateHash("1tEst", hashedPassword));
        assertEquals(false,Validator.validateHash("1tEs", hashedPassword));
    }
    
    /* Test that salted hash is different if the salt changes.
     * 
     */
    @Test
    public void testDifferentSalt() throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String salt = "123456789ABCDEF123456789ABCDEF";
        String salt2 = "123456789ABDDEFA23456C8FABC9EF";
        byte[] testSalt = Hex.decodeHex(salt.toCharArray());
        byte[] testSalt2 = Hex.decodeHex(salt2.toCharArray());
        String password = "password";
        String hashedPassword = Validator.getSaltedHash(password, testSalt).split(":")[1];
        String hashedPassword2 = Validator.getSaltedHash(password, testSalt2).split(":")[1];
        
        assertEquals(false,hashedPassword.equals(hashedPassword2));
        
        salt = "123456789ABCDEF123456789ABCDEF";
        salt2 = "223456789ABCDEF123456789ABCDEF";
        testSalt = Hex.decodeHex(salt.toCharArray());
        testSalt2 = Hex.decodeHex(salt2.toCharArray());
        password = "test123!";
        hashedPassword = Validator.getSaltedHash(password, testSalt).split(":")[1];
        hashedPassword2 = Validator.getSaltedHash(password, testSalt2).split(":")[1];
        assertEquals(false,hashedPassword.equals(hashedPassword2));      
    }
    
    /* Test that salted hash is same if the salt is the same.
     * 
     */
    @Test
    public void testSameSalt() throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String salt = "123456789ABCDEF123456789ABCDEF";
        String salt2 = "123456789ABCDEF123456789ABCDEF";
        byte[] testSalt = Hex.decodeHex(salt.toCharArray());
        byte[] testSalt2 = Hex.decodeHex(salt2.toCharArray());
        String password = "password";
        String hashedPassword = Validator.getSaltedHash(password, testSalt).split(":")[1];
        String hashedPassword2 = Validator.getSaltedHash(password, testSalt2).split(":")[1];
        
        assertEquals(true,hashedPassword.equals(hashedPassword2));
        
        salt = "123456789ABCDEF123456789ABCDEF";
        salt2 = "123456789ABCDEF123456789ABCDEF";
        testSalt = Hex.decodeHex(salt.toCharArray());
        testSalt2 = Hex.decodeHex(salt2.toCharArray());
        password = "test123!";
        hashedPassword = Validator.getSaltedHash(password, testSalt).split(":")[1];
        hashedPassword2 = Validator.getSaltedHash(password, testSalt2).split(":")[1];
        assertEquals(true,hashedPassword.equals(hashedPassword2));      
    }
}
