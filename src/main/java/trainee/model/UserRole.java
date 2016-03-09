package trainee.model;

public class UserRole {
	private Integer id;
	private Integer userId;
	private String role;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserRole() {
	}

	public UserRole(Integer userId, String role) {
		this.userId = userId;
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", userId=" + userId + ", role=" + role + "]";
	}

}
