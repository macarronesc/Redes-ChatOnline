package Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client {
	private int cookie;
	private String username;
	private Map<String, Chat> activeChats;

	public Client(int cookie, String username) {
		this.cookie = cookie;
		this.username = username;
		activeChats = new HashMap<>();
	}

	public int getCookie() {
		return cookie;
	}

	public String getUsername() {
		return username;
	}

	public Map<String,Chat> getActiveChats() {
		return activeChats;
	}

	public void addMessage(String name, String user, String message){
		Chat chat;
		if (activeChats.containsKey(name)){
			activeChats.get(name).addMessage(user, message);
		} else {
			chat = new Chat(name.equals(user));
			chat.addMessage(user, message);
			activeChats.put(name, chat);
		}
	}

}
