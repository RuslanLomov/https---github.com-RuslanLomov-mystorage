package trainee.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import trainee.dao.interf.UserDao;
import trainee.facade.interf.UserFacade;
import trainee.model.User;
import trainee.model.UserRole;

@Service
@Transactional
public class UserFacadeImpl implements UserFacade {
	private static final long serialVersionUID = 1L;
	@Autowired
	private UserDao userDAO;
	

	@Override
	public String checkUser(String login) {
		String result = "USER_NOT_EXIST";
		if (userDAO.getUserByLogin(login) != null) {
			result = "USER_EXIST";
		}
		return result;
	}

	@Override
	public int insert(User user) {
		return userDAO.insert(user);
	}

	@Override
	public int insertUserRole(UserRole role) {
		return userDAO.insertUserRole(role);
	}

	@Override
	public User getUserByLogin(String login) {
		return userDAO.getUserByLogin(login);
	}

}
