package trainee.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import trainee.dao.interf.FileDataDao;
import trainee.model.FileData;
import trainee.model.FileDataRowMapper;

public class FileDataDaoImpl extends JdbcDaoSupport implements FileDataDao {

	private static final long serialVersionUID = 1L;

	@Override
	public Integer insertIntoFiles(FileData file) {
		return getJdbcTemplate()
				.update("insert into files (file_name,file_path,fake_file_path,user_id) values(?,?,?,?)",
						new Object[] { file.getName(), file.getPath(),
								file.getFakePath(), file.getUserId() });
	}

	@Override
	public String getFileRealPath(String fakePath, Integer userId) {
		return getJdbcTemplate().queryForObject(
				"select * from files where fake_file_path=? and user_id=?",
				new Object[] { fakePath, userId }, new FileDataRowMapper()).getPath();
	}

	@Override
	public FileData getFileByPath(FileData file) {
		try {
			return getJdbcTemplate().queryForObject(
					"select * from files where file_path=? and user_id=?",
					new Object[] { file.getPath(), file.getUserId() },
					new FileDataRowMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public FileData getFileId(FileData file) {
		return getJdbcTemplate()
				.queryForObject(
						"select * from files where file_name=? and file_path=? and user_id=? LIMIT 1",
						new Object[] { file.getName(), file.getPath(), file.getUserId() },
						new FileDataRowMapper());
	}

	@Override
	public FileData getFileById(FileData file) {
		return getJdbcTemplate().queryForObject(
				"select * from files where id=? and user_id=? LIMIT 1",
				new Object[] { file.getId(), file.getUserId() }, new FileDataRowMapper());
	}

	@Override
	public Integer deleteFile(String realPath, Integer userId) {
		// delete from files where file_path=? and user_id=?
		return getJdbcTemplate().update(
				"delete from files where file_path=? and user_id=?",
				new Object[] { realPath, userId });

	}

	@Override
	public Integer renameFile(FileData file) {
		return getJdbcTemplate()
				.update("update files set file_name=? ,file_path =? , fake_file_path=? where id=? and user_id=? ",
						new Object[] { file.getName(), file.getPath(),
								file.getFakePath(), file.getId(), file.getUserId() });
	}

	@Override
	public Integer renameReferencedFile(FileData file) {
		return getJdbcTemplate()
				.update("update files set file_path =? , fake_file_path=? where id=? and user_id=? ",
						new Object[] { file.getPath(), file.getFakePath(), file.getId(),
								file.getUserId() });
	}

}
