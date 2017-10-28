package database;

import java.util.Comparator;

public class Anime {

	public String name;
	public int votes;
	public int weightedVotes;
	public int[] genresVal;
	public boolean[] genresSearch;
	public String[] genres = { "action", "adventure", "comedy", "ecchi", "fantasy", "gods", "harem", "historical",
			"magic", "mecha", "medieval", "mystery", "neet", "ninja", "otaku", "reverse_harem", "romance", "samurai",
			"sci_fi", "seinen", "shoujo", "shounen", "slice_of_life", "sports", "superhero", "superpower",
			"time_travel", "virtual_reality", "yaoi", "yuri" };

	public static Comparator<Anime> AnimeComparator = new Comparator<Anime>() {
		@Override
		public int compare(Anime x, Anime y) {
			return (x.getWeightedVotes() - y.getWeightedVotes());
		}
	};

	public Anime() {
		this.name = "";
		this.votes = 0;
		genresVal = new int[30];
		genresSearch = new boolean[30];
	}

	public Anime(String name) {
		this.name = name;
		this.votes = 0;
		genresVal = new int[30];
		genresSearch = new boolean[30];
	}
	
	public int getCategoryValue(String category) {
		return genresVal[getIndex(category)];
	}

	public int getIndex(String category) {
		for (int i = 0; i < genres.length; i++) {
			if (category.equals(genres[i]))
				return i;
		}
		return -1;
	}

	public boolean getCategoryChecked(String category) {
		return genresSearch[getIndex(category)];
	}
	
	public int[] getGenresVal()
	{
		return genresVal;
	}
			
	public boolean[] getGenresSearch()
	{
		return genresSearch;
	}
			
	public String[] getGenres()
	{
		return genres;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public void setWeightedVotes(int weightedVotes) {
		this.weightedVotes = weightedVotes;
	}


	public String getName() {
		return name;
	}

	public int getVotes() {
		return votes;
	}

	public int getWeightedVotes() {
		return weightedVotes;
	}
	
	public void setGenresVal(int[] genresVal)
	{
		this.genresVal = genresVal;
	}
			
	public void getGenresSearch(boolean[] genresSearch)
	{
		this.genresSearch = genresSearch;
	}
			
	public void getGenres(String[] genres)
	{
		this.genres = genres;
	}

}
