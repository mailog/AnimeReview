package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class VoteMapper implements RowMapper<Vote>{
	@Override
	public Vote mapRow(ResultSet rs, int rowNum) throws SQLException{
		Vote vote = new Vote();
		vote.setCategory(rs.getString("category"));
		vote.setAnime(rs.getString("anime"));
		vote.setUsername(rs.getString("username"));
		return vote;
	}

}
