package Waifu.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import database.Account;
import database.AccountJDBC;
import database.Anime;
import database.AnimeJDBC;
import database.NewMessage;
import database.Vote;
import database.VoteJDBC;

@Controller
public class WebController extends WebMvcConfigurerAdapter {

	public static ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
	public static AccountJDBC accountJDBC = (AccountJDBC) context.getBean("accountJDBCTemplate");
	public static AnimeJDBC animeJDBC = (AnimeJDBC) context.getBean("animeJDBCTemplate");
	public static VoteJDBC voteJDBC = (VoteJDBC) context.getBean("voteJDBCTemplate");
	
	
	@RequestMapping(value = "/dontdoit", method = RequestMethod.GET)
	String getWebScrape() throws IOException
	{
		int counter = 0;
		for(int i = 1; i < 296; i++){
			Document doc = Jsoup.connect("http://www.anime-planet.com/anime/all?page=" + i).get();
			Elements img = doc.getElementsByTag("img");
			for(Element el : img)
			{
				if(el.attr("alt") != null && !el.attr("alt").isEmpty())
				{
					try
					{
						animeJDBC.createAnime(el.attr("alt"));
						counter++;
						System.out.println(counter);
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("DONE!!!");
		return "redirect:/";
	}
	
	/*@RequestMapping(value = "/livechat", method = RequestMethod.GET)
	String getLiveChat() throws IOException
	{
		File chat = new File("/txt/livechat.txt");
		Scanner reader = new Scanner(chat);
		String contents = "";
		while(reader.hasNextLine())
		{
			contents += reader.nextLine();
		}
		System.out.println(contents +"!!");
		reader.close();
		return contents;
	}*/
	
	@RequestMapping(value = "/", method = RequestMethod.GET) 
	ModelAndView getHome(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser) throws IOException{
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		modelAndView.addObject("newMessage", new NewMessage());
		return modelAndView;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	String postHome(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser, @ModelAttribute("newMessage") NewMessage newMessage)
	{
		
		System.out.println(newMessage.getMessage());
		try
		{
			FileWriter fileWriter = new FileWriter("static/txt/livechat.txt", true);
			fileWriter.append("\n" + currentuser + ": " + newMessage.getMessage());
			fileWriter.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/account", method = RequestMethod.GET) 
	ModelAndView getAccount(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser){
		ModelAndView modelAndView = new ModelAndView("account");
		modelAndView.addObject("votes", voteJDBC.getVotes(currentuser));
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	ModelAndView getSignup(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser) {
		ModelAndView modelAndView = new ModelAndView("signup");
		modelAndView.addObject("signup_user", new Account());
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	ModelAndView postSignup(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser, @ModelAttribute("signup_user") Account user, BindingResult bindingResult,
			HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("signup-result");
		try {
			accountJDBC.createUser(user);
			modelAndView.addObject("newuser", user);
			modelAndView.addObject("validity", true);
		} catch (Exception e) {
			e.printStackTrace();
			modelAndView.addObject("validity", false);
		}
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	ModelAndView getLogin(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser) {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("login_user", new Account());
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	ModelAndView postLogin(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser, @ModelAttribute("login_user") Account user, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("login-result");
		boolean validity;
		Account userChk = new Account();
		try {
			userChk = accountJDBC.getUser(user.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user.getPassword().equals(userChk.getPassword())) {
			validity = true;
		} else {
			validity = false;
		}
		if (validity) {
			response.addCookie(new Cookie("USERNAME", user.getName()));
		}
		modelAndView.addObject("user", user.getName());
		modelAndView.addObject("validity", validity);
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	String getLogout(@CookieValue(value = "USERNAME", defaultValue="null") String username, HttpServletRequest request, HttpServletResponse response)
	{
		username = "null";
		Cookie[] cookies = request.getCookies();
		for(int i = 0; i < cookies.length; i++)
		{
			cookies[i].setValue("null");
			cookies[i].setMaxAge(0);
			response.addCookie(cookies[i]);
		}
		return "redirect:/";
	}
	
	
	@RequestMapping(value = "/anime/{page}", method = RequestMethod.GET)
	ModelAndView getAnimes(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser, @PathVariable("page") String page){
		int limit = Integer.parseInt(page);
		int min = (limit - 1)* 10;
		int max = limit * 10;
		List<Anime> allAnime = animeJDBC.getAnimes();
		List<Anime> pageAnime = allAnime.subList(min, max);
		ModelAndView modelAndView = new ModelAndView("animes");
		modelAndView.addObject("page", limit);
		modelAndView.addObject("animes", pageAnime);
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}
	

	@RequestMapping(value = "/{anime}/", method = RequestMethod.GET)
	ModelAndView getAnimeDetails(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser, @PathVariable("anime") String anime){
		ModelAndView modelAndView = new ModelAndView("animedetails");
		modelAndView.addObject("anime", animeJDBC.getAnime(anime));
		modelAndView.addObject("animevote", new Anime(anime));
		modelAndView.addObject("hasVoted", voteJDBC.hasVoted(anime, currentuser));
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}
	
	@RequestMapping(value = "/{anime}/", method = RequestMethod.POST)
	String postAnimeDetails(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser, @PathVariable("anime") String anime, @ModelAttribute("animevote") Anime animeVote, BindingResult bindingResult,
			HttpServletRequest request){
		animeVote.setName(anime);
		for(int i = 0; i < animeVote.genres.length; i++)
		{
			if(animeVote.genresSearch[i])
			{
				Vote addedVote = new Vote(animeVote.genres[i], animeVote.name, currentuser);
				animeJDBC.incrementVote(animeVote.name, animeVote.genres[i]);
				voteJDBC.createVote(addedVote);
			}
		}
		return "redirect:/" + anime + "/";
	}
	
	@RequestMapping(value = "/{anime}/update", method = RequestMethod.POST)
	String postAnimeDetailsUpdateVote(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser, @PathVariable("anime") String anime, @ModelAttribute("animevote") Anime animeVote, BindingResult bindingResult,
			HttpServletRequest request){
		animeVote.setName(anime);
		List<Vote> deletedVote = voteJDBC.deleteVote(anime, currentuser);
		for(int i = 0; i < deletedVote.size(); i++)
		{
			animeJDBC.decrementVote(anime, deletedVote.get(i).category);
		}
		for(int i = 0; i < animeVote.genres.length; i++)
		{
			if(animeVote.genresSearch[i])
			{
				Vote addedVote = new Vote(animeVote.genres[i], animeVote.name, currentuser);
				animeJDBC.incrementVote(animeVote.name, animeVote.genres[i]);
				voteJDBC.createVote(addedVote);
			}
		}
		return "redirect:/" + anime + "/";
	}

	@RequestMapping(value = "/animesearch", method = RequestMethod.GET)
	ModelAndView getAnimeSearch(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser) {
		ModelAndView modelAndView = new ModelAndView("animesearch");
		modelAndView.addObject("animesearch", new Anime());
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}

	@RequestMapping(value = "/animesearch", method = RequestMethod.POST)
	ModelAndView postAnimeSearch(@CookieValue(value = "USERNAME", defaultValue = "null") String currentuser,
			@ModelAttribute("animesearch") Anime animeSearch, BindingResult bindingResult,
			HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("animesearch-result");
		List<String> searchedBy = new ArrayList<String>();
		for(int i = 0; i < animeSearch.genres.length; i++)
		{
			if(animeSearch.genresSearch[i])
			{
				searchedBy.add(animeSearch.genres[i]);
			}
		}
		modelAndView.addObject("searchedBy", searchedBy);
		modelAndView.addObject("animes", animeJDBC.getAnimes(animeSearch));
		modelAndView.addObject("loggedIn", !currentuser.equals("null"));
		modelAndView.addObject("currentuser", currentuser);
		return modelAndView;
	}
}