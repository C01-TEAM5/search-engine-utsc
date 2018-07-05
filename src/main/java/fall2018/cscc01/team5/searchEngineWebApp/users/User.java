package fall2018.cscc01.team5.searchEngineWebApp.users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import fall2018.cscc01.team5.searchEngineWebApp.util.UserValidator;

public class User {
 
	private String email; 
	private String name; 
	private String username;
	private String hash;
	
	public User(String username, String email, String name, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.username = username; 
		this.email = email; 
		this.name = name; 
		this.hash = UserValidator.getSaltedHash(password, UserValidator.getSalt());
	}
	
	/**
	 * Get users name
	 * @return  username
	 */
	public String getUsername() {
		return username;
	}
	
	
	/**
	 * Get email
	 * @return email
	 */
	public String getEmail() {
		return this.email;
	}
	
	
	/**
	 * get Name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
		
	/**
	 * get secure password/hash
	 * @return hash
	 */
	public String getHash() {
		return this.hash;
	}
	
	
	/**
	 * Return a string representation of this user. 
	 * 
	 * @return a string representation of this user
	 */
	@Override
	public String toString() {
		String result = "User: [Username: " + username 
				+ " email: " + email 
				+ " name: " + name 
				+ " hashPassword: " + hash +  "]";
		return result;
	}
}

