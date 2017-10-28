package database;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class AccountJDBC implements AccountDAO{
	
	private static DataSource datasource;
	private static JdbcTemplate jdbcTemplateObject;
	
	@Override
	public void setDataSource(DataSource ds){
		AccountJDBC.datasource = ds;
		jdbcTemplateObject = new JdbcTemplate(datasource);
	}
	
	@Override
	public void createUser(Account account)
	{
		String SQL = "insert into accounts (name, email, password) values (?, ?, ?)";
		jdbcTemplateObject.update(SQL, account.getName(), account.getEmail(), account.getPassword());
		System.out.println("Created Record Name: " +"\nUsername: " + account.getEmail() + "\nEmail: " + account.getName() + "\nPW: " + account.getPassword());
	}
	
	@Override
	public Account getUser(String name)
	{
		String SQL = "select * from accounts where name=?";
		Account user = jdbcTemplateObject.queryForObject(SQL, new Object[]{name}, new AccountMapper());
		System.out.println("Got " + user.getEmail());
		return user;
	}
	
	@Override
	public List<Account> listUsers()
	{
		String SQL = "select * from accounts";
		List<Account> users = jdbcTemplateObject.query(SQL, new AccountMapper());
		return users;
	}
	
	@Override
	public void deleteUser(String email)
	{
		String SQL = "delete from accounts where name = ?";
		jdbcTemplateObject.update(SQL, email);
		System.out.println("Deleted " + email + " from Users");
	}
	
	@Override
	public void updateUser(String email, String password)
	{
		String SQL = "update accounts set password = ? where name = ?";
		jdbcTemplateObject.update(SQL, password, email);
		System.out.println("Updated " + email);
	}
}
