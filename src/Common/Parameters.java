package Common;

public class Parameters {

	public static String SERVER_IP = "localhost";
	public static int LISTEN_PORT = 1337;
	public static int CLIENT_LISTEN_PORT = 666;	//TODO: OJO SOLO PARA TESTEAR!!!!
	public static int MAX_BUFFER_SIZE = 2000;	//Max size of any given message
	public static String HAS_MORE_DATA = "";	//TODO, for when message exceeds the max buffer size
	public static int MAX_NAME_LENGTH = 20;		//Max user or group name length
	public static String SEPARATOR = "\0";	//To separate data in a message
	public static int TIMEOUT_DEFAULT = 1 * 1000;	// 10 second timeout


}
