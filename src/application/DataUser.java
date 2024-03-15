package application;

public class DataUser {
	String id_user;
	String email;
	String password;
	String phone_number;
	String gender;
	String role;
	public DataUser(String id_user, String email, String password, String phone_number, String gender, String role) {
		super();
		this.id_user = id_user;
		this.email = email;
		this.password = password;
		this.phone_number = phone_number;
		this.gender = gender;
		this.role = role;
	}
	public String getId_user() {
		return id_user;
	}
	public void setId_user(String id_user) {
		this.id_user = id_user;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
