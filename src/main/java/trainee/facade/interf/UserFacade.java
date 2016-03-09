package trainee.facade.interf;

import java.io.Serializable;

import trainee.model.User;
import trainee.model.UserRole;

public interface UserFacade extends Serializable {
	public String checkUser(String login);

	public int insert(User user);

	public int insertUserRole(UserRole role);

	public User getUserByLogin(String login);

}
