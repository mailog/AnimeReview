package database;
import java.util.List;
import javax.sql.DataSource;
public interface AccountDAO {
	
	public void setDataSource(DataSource ds);
	
	public void createUser(Account user);
	
	public Account getUser(String name);
	
	public List<Account> listUsers();
	
	public void deleteUser(String email);
	
	public void updateUser(String email, String password);
}
