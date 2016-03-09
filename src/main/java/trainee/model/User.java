package trainee.model;

public class User {
	private Integer id;
	private String login;
	private String password;
	private Integer enabled;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public User() {
	}

	public User(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public User(Integer id, String login, String password) {
		this.id = id;
		this.login = login;
		this.password = password;
	}

	public boolean equals(Object user) {
		if (this == user) {
			return true;
		}
		if (user instanceof User) {
			return true;
		}
		User thisUser = (User) user;
		return this.id.equals(thisUser.getId()) && this.login.equals(thisUser.getLogin())
				&& this.password.equals(thisUser.getPassword());
	}

	public int hashCode() {
		int hash = 23;
		hash = (hash * 7 + this.getId().hashCode() + this.getLogin().hashCode() + this
				.getPassword().hashCode());
		return hash;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password
				+ ", enabled=" + enabled + "]";
	}

}
