package database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnimeJDBC implements AnimeDAO {

	private static DataSource datasource;
	private static JdbcTemplate jdbcTemplateObject;

	@Override
	public void setDataSource(DataSource ds) {
		AnimeJDBC.datasource = ds;
		jdbcTemplateObject = new JdbcTemplate(datasource);
	}

	@Override
	public void createAnime(Anime anime) {
		String SQL = "insert into anime (name) values (?)";
		jdbcTemplateObject.update(SQL, anime.getName());
	}
	
	@Override
	public void createAnime(String name) {
		String SQL = "insert into anime (name) values (?)";
		jdbcTemplateObject.update(SQL, name);
	}

	@Override
	public Anime getAnime(String name) {
		String SQL = "select * from anime where name=?";
		Anime user = jdbcTemplateObject.queryForObject(SQL, new Object[] {name}, new AnimeMapper());
		return user;
	}
	
	@Override
	public List<Anime> getAnimes()
	{
        String SQL = "select * from anime";  
        return jdbcTemplateObject.query(SQL, new AnimeMapper());
	}

	@Override
	public List<Anime> getAnimes(Anime anime)
    {
        String SQL = "";
        List<Anime> animes = new ArrayList<Anime>();
        for(int i = 0; i < anime.genres.length; i++)
        {
            if(anime.genresSearch[i])
            {
                SQL = "select * from anime where " + anime.genres[i] + " > 0";  
                animes.addAll(jdbcTemplateObject.query(SQL, new AnimeMapper()));
            }
        }
        for(int i = 0; i < animes.size(); i++)
        {
            int weightedVote = 0;
            for(int j = 0; j < anime.genres.length; j++)
            {  
                if(anime.genresSearch[j])
                {
                    weightedVote += animes.get(i).getCategoryValue(anime.genres[j]);
                }
            }
            animes.get(i).setWeightedVotes(weightedVote);
        }
        Collections.sort(animes, Anime.AnimeComparator);
        return animes;
    }

	@Override
	public void incrementVote(String anime, String category) {
		String SQL = "update anime set " + category + " = " + category + " + 1 where name = ?";
		jdbcTemplateObject.update(SQL, anime);
	}
	
	@Override
	public void decrementVote(String anime, String category) {
		String SQL = "update anime set " + category + " = " + category + " - 1 where name = ?";
		jdbcTemplateObject.update(SQL, new Object[]{anime});
	}
}
