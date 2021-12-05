package Common;

import java.util.HashMap;
import java.util.Map;

public class Client {
	private final int cookie;
	private final String username;
	private final Map<String, Chat> activeChats;

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

	public String[] getActiveChats() {
		return activeChats.keySet().toArray(new String[0]);
	}

	public int getUnread() {
		int unread = 0;
		for (Map.Entry<String, Chat> chat : activeChats.entrySet()){
			unread += chat.getValue().getUnread();
		}
		return unread;
	}

	/**
	 * Adds a message to the user's chats
	 * @param name Name of the chat
	 * @param user Name of the user sending the message
	 * @param message The chat message
	 */
	public synchronized void addMessage(String name, String user, String message){
		Chat chat;
		if (activeChats.containsKey(name)){
			activeChats.get(name).addMessage(user, message);
		} else {
			chat = new Chat(name.equals(user));
			chat.addMessage(user, message);
			activeChats.put(name, chat);
		}
	}

	/**
	 * Closes a chat
	 * @param name Name of the chat to be closed
	 */
	public void closeChat(String name){
		activeChats.remove(name);
	}
}
