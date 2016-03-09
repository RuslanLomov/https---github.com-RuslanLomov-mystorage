package trainee.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRoleRowMapper implements RowMapper<UserRole> {

	@Override
	public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserRole userRole = new UserRole();
		userRole.setId(rs.getInt("id"));
		userRole.setUserId(rs.getInt("user_id"));
		userRole.setRole(rs.getString("role"));
		return userRole;
	}

}
