package trainee.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class FileDataRowMapper implements RowMapper<FileData> {

	@Override
	public FileData mapRow(ResultSet rs, int rowNum) throws SQLException {
		FileData file = new FileData();
		file.setId(rs.getInt("id"));
		file.setName(rs.getString("file_name"));
		file.setPath(rs.getString("file_path"));
		file.setFakePath(rs.getString("fake_file_path"));
		file.setUserId(rs.getInt("user_id"));
		return file;
	}

}
