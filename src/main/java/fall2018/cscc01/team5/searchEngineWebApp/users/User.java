package fall2018.cscc01.team5.searchEngineWebApp.users;

import fall2018.cscc01.team5.searchEngineWebApp.util.UserValidator;

public class User {
 
	private String email; 
	private String name; 
	private String username;
	private byte[] salt;
	private String hash;
	
	public User(String username, String email, String name, String password) {
		this.username = username; 
		this.email = email; 
		this.name = name; 
		this.salt = UserValidator.getSalt();
		this.hash = UserValidator.get_PBKDF2_SecurePassword(password);
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
	 * get Salt, the random data that append to password before
	 * obtaining hash.
	 * @return salt
	 */
	public byte[] getSalt() {
		return this.salt;
	}
	
	
	/**
	 * get secure password/hash
	 * @return hash
	 */
	public String getHash() {
		return this.hash;
	}
}

