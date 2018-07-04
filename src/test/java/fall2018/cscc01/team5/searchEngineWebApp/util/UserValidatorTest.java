package fall2018.cscc01.team5.searchEngineWebApp.util;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class UserValidatorTest {

    @Test
    public void testHash() throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String salt = "123456789ABCDEF123456789ABCDEF";
        String salt2 = "A23456789ABCDEF123456789ABCDEF";
        byte[] testSalt = Hex.decodeHex(salt.toCharArray());
        byte[] testSalt2 = Hex.decodeHex(salt2.toCharArray());
        
        String password = "password";
        
        String hashedPassword = UserValidator.getSaltedHash(password, testSalt);
        String hashedPassword2 = UserValidator.getSaltedHash("password", testSalt2);
        
        assertEquals(false,UserValidator.validatePassword("", hashedPassword));
        assertEquals(true,UserValidator.validatePassword("password", hashedPassword));
        
        
    }
    
}
