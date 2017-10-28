package database;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class VoteJDBC implements VoteDAO{
	
	private static DataSource datasource;
	private static JdbcTemplate jdbcTemplateObject;
	
	@Override
	public void setDataSource(DataSource ds){
		VoteJDBC.datasource = ds;
		jdbcTemplateObject = new JdbcTemplate(datasource);
	}
	
	@Override
	public void createVote(Vote vote)
	{
		String SQL = "insert into votes (category, anime, username) values (?, ?, ?)";
		jdbcTemplateObject.update(SQL, vote.getCategory(), vote.getAnime(), vote.getUsername());
	}
	
	@Override
	public List<Vote> deleteVote(String anime, String username)
	{
		String SQL = "select * from votes where anime = ? and username = ?";
		List<Vote> deletedVotes = jdbcTemplateObject.query(SQL, new Object[]{anime, username}, new VoteMapper());
		SQL = "delete from votes where username = ?";
		jdbcTemplateObject.update(SQL, username);
		return deletedVotes;
	}
	
	@Override
	public List<Vote> getVotes(String username)
	{
		String SQL = "select * from votes where username = ?";
		List<Vote> vote = jdbcTemplateObject.query(SQL, new Object[]{username}, new VoteMapper());
		return vote;
	}
	
	@Override
	public boolean hasVoted(String anime, String username)
	{
		String SQL = "select * from votes where anime = ? and username = ?";
		List<Vote> vote = jdbcTemplateObject.query(SQL, new Object[]{anime, username}, new VoteMapper());
		return (vote.size() != 0);
	}
	
}
