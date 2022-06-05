package gr.ntua.ece.db.hfri.erp;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.nio.charset.StandardCharsets;

import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.json.JSONException;

public class Settings {
	
	private static JSONObject settings;
	private static Path settingsPath = Paths.get("settings.json");
	private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	
	public static boolean exists() {
		return Files.exists(settingsPath);
	}
	
	public static boolean isReadable() {
		return Files.isReadable(settingsPath);
	}
	
	public static void load() throws JSONException, IOException {
		settings = new JSONObject(Files.readString(settingsPath, StandardCharsets.UTF_8));
		cache.clear();
	}
	
	public static void save() throws IOException {
		Files.writeString(settingsPath, settings.toString(1), StandardCharsets.UTF_8);
	}
	
	public static void exportDefault() throws IOException {
		JSONObject defaultSettings = new JSONObject();
		
		defaultSettings.put("database",
			new JSONObject()
				.put("type", "mysql")
				.put("host", "127.0.0.1")
				.put("port", 3306)
				.put("schema", "hfri")
				.put("allow-public-key-retrieval", false)
			);
		
		defaultSettings.put("ssh",
			new JSONObject()
				.put("tunnel", false)
				.put("host", "")
				.put("port", 22)
				.put("user", "")
				.put("password", "")
				.put("strict-host-key-checking",
					new JSONObject()
						.put("enabled", false)
						.put("known-hosts-file", "known_hosts"))
				.put("rsa-authentication",
					new JSONObject()
						.put("enabled", false)
						.put("key-file", "")
						.put("passphrase", ""))
			);
		
		defaultSettings.put("miscellaneous",
			new JSONObject()
				.put("random-avatars", false)
				.put("client-side-filtering", false)
			);
		
		Files.writeString(settingsPath, defaultSettings.toString(1), StandardCharsets.UTF_8);
	}
	
	public static void set(String id, boolean value) {
		String[] address = id.split("\\.");
		
		JSONObject container = settings;
		for(int i = 0; i < address.length; i++) {
			if(i == address.length - 1) {
				container.put(address[i], value);
				cache.put(id, value);
			} else {
				if(container.has(address[i])) {
					if(!(container.get(address[i]) instanceof JSONObject)) container.put(address[i], new JSONObject());
				} else container.put(address[i], new JSONObject());
				
				container = container.getJSONObject(address[i]);
			}
		}
	}
	
	public static void set(String id, int value) {
		String[] address = id.split("\\.");
		
		JSONObject container = settings;
		for(int i = 0; i < address.length; i++) {
			if(i == address.length - 1) {
				container.put(address[i], value);
				cache.put(id, value);
			} else {
				if(container.has(address[i])) {
					if(!(container.get(address[i]) instanceof JSONObject)) container.put(address[i], new JSONObject());
				} else container.put(address[i], new JSONObject());
				
				container = container.getJSONObject(address[i]);
			}
		}
	}
	
	public static void set(String id, String value) {
		String[] address = id.split("\\.");
		
		JSONObject container = settings;
		for(int i = 0; i < address.length; i++) {
			if(i == address.length - 1) {
				container.put(address[i], value);
				cache.put(id, value);
			} else {
				if(container.has(address[i])) {
					if(!(container.get(address[i]) instanceof JSONObject)) container.put(address[i], new JSONObject());
				} else container.put(address[i], new JSONObject());
				
				container = container.getJSONObject(address[i]);
			}
		}
	}
	
	public static boolean getBoolean(String id) {
		if(cache.containsKey(id)) return (boolean) cache.get(id);
		else {
			String[] address = id.split("\\.");
			
			try {
				JSONObject container = settings;
				
				for(int i = 0; i < address.length; i++) {
					if(i == address.length - 1) {
						boolean entry = container.getBoolean(address[i]);
						
						cache.put(id, entry);
						return entry;
					} else container = container.getJSONObject(address[i]);
				}
			} catch(JSONException ex) {}
			
			return false;
		}
	}
	
	public static int getInt(String id) {
		if(cache.containsKey(id)) return (int) cache.get(id);
		else {
			String[] address = id.split("\\.");
			
			try {
				JSONObject container = settings;
				
				for(int i = 0; i < address.length; i++) {
					if(i == address.length - 1) {
						int entry = container.getInt(address[i]);
						
						cache.put(id, entry);
						return entry;
					} else container = container.getJSONObject(address[i]);
				}
			} catch(JSONException ex) {}
			
			return -1;
		}
	}
	
	public static String getString(String id) {
		if(cache.containsKey(id)) return (String) cache.get(id);
		else {
			String[] address = id.split("\\.");
			
			try {
				JSONObject container = settings;
				
				for(int i = 0; i < address.length; i++) {					
					if(i == address.length - 1) {
						String entry = container.getString(address[i]);
						
						cache.put(id, entry);
						return entry;
					} else container = container.getJSONObject(address[i]);
				}
			} catch(JSONException ex) {}
			
			return null;
		}
	}
	
}