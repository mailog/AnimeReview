package database;

public class Vote {
	public String category;
	public String anime;
	public String username;
	
	public Vote()
	{
		category = "";
		anime = "";
		username = "";
	}
	
	public Vote(String category, String anime, String username)
	{
		this.category = category;
		this.anime = anime;
		this.username = username;
	}
	
	public void setCategory(String category)
	{
		this.category = category;
	}
	
	public void setAnime(String anime)
	{
		this.anime = anime;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public String getAnime()
	{
		return anime;
	}
	
	public String getUsername()
	{
		return username;
	}
}
