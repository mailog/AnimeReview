package database;
import java.util.List;
import javax.sql.DataSource;
public interface AnimeDAO {
	
	public void setDataSource(DataSource ds);
	
	public void createAnime(Anime anime);
	
	public void createAnime(String name);
	
	public Anime getAnime(String name);
	
	public List<Anime> getAnimes();
	
	public List<Anime> getAnimes(Anime anime);
	
	public void incrementVote(String name, String category);
	
	public void decrementVote(String anime, String category);
	
}
