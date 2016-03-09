package trainee.test;

import java.io.File;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import trainee.model.FileData;
import trainee.model.FileDataRowMapper;

public class Test {
	public static void main(String[] args) {
		SingleConnectionDataSource ds = new SingleConnectionDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/file_storage");
		ds.setUsername("root");
		ds.setPassword("");
		JdbcTemplate jt = new JdbcTemplate(ds);
		File f = new File("D:/javaTest/1/xaxabbbbmn234");
		Object o = jt
				.update("update files set file_name=? ,file_path =? , fake_file_path=? where id=? and user_id=?",
						new Object[] { "xaxabbbbmn234", f.getPath(),
								"src/xaxabbbbmn234/", 124, 2 });
		System.out.println(o);
		ds.destroy();
	}
}
