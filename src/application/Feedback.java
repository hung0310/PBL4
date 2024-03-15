package application;

public class Feedback {
	String id_user;
	String content;
	String email;
	
	public Feedback(String id_user, String content, String email) {
		super();
		this.id_user = id_user;
		this.content = content;
		this.email = email;
	}
	
	public String getId_user() {
		return id_user;
	}
	public void setId_user(String id_user) {
		this.id_user = id_user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}