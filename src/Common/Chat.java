package Common;

import java.util.ArrayList;

public class Chat {
	private ArrayList<ChatMessage> messages;
	private boolean group;

	public Chat(boolean group) {
		this.group = group;
	}

	public boolean isGroup() {
		return group;
	}

	public ArrayList<ChatMessage> getMessages() {
		return messages;
	}

	public void addMessage(String user, String message){
		messages.add(new ChatMessage(user, message));
	}
}
