package Common;


public class ChatMessage {
	private String user;
	private String message;

	//Optioins to manage the messages of the chat
	public ChatMessage(String user, String message) {
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
