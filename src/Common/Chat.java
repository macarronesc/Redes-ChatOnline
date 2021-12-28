package Common;

import java.util.ArrayList;
import java.util.List;

public class Chat {
	private List<ChatMessage> messages;
	private boolean group;
	private int unread;

	//Options to manage chat
	public Chat(boolean group) {
		messages = new ArrayList<>();
		unread = 0;
		this.group = group;
	}

	public boolean isGroup() {
		return group;
	}

	public List<ChatMessage> getMessages() {
		return messages;
	}

	public void addMessage(String user, String message) {
		unread++;
		messages.add(new ChatMessage(user, message));
	}

	public int getUnread() {
		return unread;
	}

	public void markRead() {
		unread = 0;
	}
}
