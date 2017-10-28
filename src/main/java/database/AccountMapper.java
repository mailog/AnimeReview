package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AccountMapper implements RowMapper<Account>{
	@Override
	public Account mapRow(ResultSet rs, int rowNum) throws SQLException{
		Account user = new Account();
		user.setEmail(rs.getString("email"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		return user;
	}

}
