package fall2018.cscc01.team5.searchEngineWebApp.util;

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
        String hashedPassword = UserValidator.getSaltedHash(password, testSalt);        
        assertEquals(true,UserValidator.validatePassword("password", hashedPassword));
        
        salt = "123456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "test123";
        hashedPassword = UserValidator.getSaltedHash(password, testSalt);      
        assertEquals(true,UserValidator.validatePassword("test123", hashedPassword));
        
        salt = "AAA456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "!tEst";
        hashedPassword = UserValidator.getSaltedHash(password, testSalt);      
        assertEquals(true,UserValidator.validatePassword("!tEst", hashedPassword));
    }
    
    /* Test invalid passwords
     */
    @Test
    public void testInvalidHash() throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String salt = "123456789ABCDEF123456789ABCDEF";
        byte[] testSalt = Hex.decodeHex(salt.toCharArray());     
        String password = "password";
        String hashedPassword = UserValidator.getSaltedHash(password, testSalt);        
        assertEquals(false,UserValidator.validatePassword("passwor", hashedPassword));
        assertEquals(false,UserValidator.validatePassword("pass", hashedPassword));
        assertEquals(false,UserValidator.validatePassword("hellothe", hashedPassword));
        
        salt = "123456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "test123";
        hashedPassword = UserValidator.getSaltedHash(password, testSalt);      
        assertEquals(false,UserValidator.validatePassword("test1234", hashedPassword));
        assertEquals(false,UserValidator.validatePassword("test", hashedPassword));
        assertEquals(false,UserValidator.validatePassword("TEST123", hashedPassword));
        assertEquals(false,UserValidator.validatePassword("t!st123", hashedPassword));
        
        salt = "AAA456789ABCDEF123456789123345";
        testSalt = Hex.decodeHex(salt.toCharArray());     
        password = "!tEst";
        hashedPassword = UserValidator.getSaltedHash(password, testSalt);      
        assertEquals(false,UserValidator.validatePassword("", hashedPassword));
        assertEquals(false,UserValidator.validatePassword("1tEst", hashedPassword));
        assertEquals(false,UserValidator.validatePassword("1tEs", hashedPassword));
    }
}
