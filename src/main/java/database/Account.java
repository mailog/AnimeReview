package database;

public class Account {
	public String email;
	public String name;
	public String password;
	
	public Account(){	
		this.email="";
		this.name = "";
		this.password = "";
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public void setName(String name)
	{
		this.name = name;	
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getPassword(){
		return password;
	}

}
