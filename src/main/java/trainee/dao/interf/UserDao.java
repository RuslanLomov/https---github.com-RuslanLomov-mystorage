package trainee.dao.interf;

import java.io.Serializable;

import trainee.model.User;
import trainee.model.UserRole;

public interface UserDao extends Serializable {
	public int insert(User user);

	public int insertUserRole(UserRole role);

	public User getUserByLogin(String login);
}
