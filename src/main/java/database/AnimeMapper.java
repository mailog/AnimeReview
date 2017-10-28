package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AnimeMapper implements RowMapper<Anime>{
    @Override
	public Anime mapRow(ResultSet rs, int rowNum) throws SQLException{
        Anime anime = new Anime();
        anime.setName(rs.getString("name"));
        anime.setVotes(rs.getInt("votes"));
        for(int i = 0; i < anime.genres.length; i++)
        {
            anime.genresVal[i] = rs.getInt(anime.genres[i]);
        }
        return anime;
    }
 
}
