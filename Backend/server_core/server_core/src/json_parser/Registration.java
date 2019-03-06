package json_parser;

import com.google.gson.Gson;

public class Registration {
	public String request = null;
	public String username = null;
	public String pass = null;
	public String privileges = null;
	
	public String toString (Registration reg) {
		Gson gson = new Gson();
		
		
		return gson.toJson(reg);
		
	}
		
		
	

}
