package Common;

public class MessageToChat { //implements Runnable {
	private Message message;
	private boolean hasMore;
	private Client user;

	public MessageToChat(Message message, Client user) {
		this.user = user;
		this.message = message;
		hasMore = false;
	}

	public void run() {
		String[] text = message.getData().split(Parameters.SEPARATOR);

		if (text.length == 3){
			user.addMessage(text[0],text[1],text[2]);
		} else {
			System.out.println("Error converting message " + text.length); //TODO
		}
	}
}
