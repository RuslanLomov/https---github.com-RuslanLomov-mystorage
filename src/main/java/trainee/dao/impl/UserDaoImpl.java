package trainee.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import trainee.dao.interf.UserDao;
import trainee.model.User;
import trainee.model.UserRole;

@Repository
@Transactional
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	public int insert(User user) {
		return getJdbcTemplate().update("insert into users (login,password) values(?,?)",
				new Object[] { user.getLogin(), user.getPassword() });
	}

	@Override
	public int insertUserRole(UserRole role) {
		return getJdbcTemplate().update(
				"insert into user_roles (user_id,role) values(?,?)",
				new Object[] { role.getUserId(), role.getRole() });
	}

	public User getUserByLogin(String login) {
		try {
			return (User) getJdbcTemplate().queryForObject(
					"select id,login from users where login=?", new Object[] { login },
					new BeanPropertyRowMapper<User>(User.class));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
