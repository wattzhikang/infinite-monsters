package json_parser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Registration {
	public JsonElement request = null;
	public JsonElement username = null;
	public JsonElement pass = null;
	public JsonElement privileges = null;
	
	public String toString (Registration reg) {
		Gson gson = new Gson();
		
		
		return gson.toJson(reg);
		
	}
		
		
	

}
