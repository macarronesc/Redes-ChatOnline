package Common;

import java.time.Instant;

public class ChatMessage {
	private String user;
	private String message;
	private Instant time;

	public ChatMessage(String user, String message) {
		time = Instant.now();
		this.user = user;
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public String getMessage() {
		return message;
	}
}
