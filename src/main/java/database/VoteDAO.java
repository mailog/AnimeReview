package database;
import java.util.List;

import javax.sql.DataSource;
public interface VoteDAO {
	
	public void setDataSource(DataSource ds);
	
	public void createVote(Vote vote);
	
	public List<Vote> deleteVote(String anime, String username);
	
	public List<Vote> getVotes(String username);
	
	public boolean hasVoted(String anime, String username);
}
