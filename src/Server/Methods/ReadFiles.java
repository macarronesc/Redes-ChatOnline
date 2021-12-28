package Server.Methods;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ReadFiles {

	/**
	 * Gets the active chats from a file. The key of the map is the creator user and the value is the participant users
	 *
	 * @param fileName The filename containing the chats
	 * @return Hashmap containing the active chats
	 */
	public static HashMap<String, String[]> getDataChats(String fileName) {
		HashMap<String, String[]> data = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(fileName))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] lineAux = line.split("\\(");
				String[] chats = lineAux[1].substring(0, lineAux[1].length() - 1).split(";");
				data.put(lineAux[0], chats);
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR. File not found!");
			e.printStackTrace();
			return null;
		}
		return data;
	}

	/**
	 * Saves the active chats to a file
	 *
	 * @param data     The active chats
	 * @param fileName A filename
	 * @return Error or informational message
	 */
	public static String saveDataChats(HashMap<String, String[]> data, String fileName) {
		try (Writer writer = new FileWriter(fileName)) {
			String text = "";
			for (String user : data.keySet()) {
				text = text.concat(user).concat("(");
				for (String chat : data.get(user)) {
					text = text.concat(chat).concat(";");
				}
				text = text.substring(0, text.length() - 1);
				text = text.concat(")").concat(System.getProperty("line.separator"));
			}
			writer.append(text);
			return "Success saving data of chats";
		} catch (IOException e) {
			return "ERROR. Could not save data!" + e;
		}
	}

	/**
	 * Gets the user's info
	 *
	 * @param fileName The filename where users are stored
	 * @return A hashmap for the users
	 */
	public static HashMap<String, String> getDataUsers(String fileName) {
		HashMap<String, String> data = new HashMap<>();

		try (Scanner scanner = new Scanner(new File(fileName))) {
			while (scanner.hasNextLine()) {
				String[] users = scanner.nextLine().split(",");
				data.put(users[0], users[1]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR. File not found!");
			e.printStackTrace();
			return null;
		}
		return data;
	}

	/**
	 * Saves information about the users to a file
	 *
	 * @param data     The data to be saved
	 * @param fileName The filename where users are stored
	 * @return Error or informational message
	 */
	public static String saveDataUsers(HashMap<String, String> data, String fileName) {
		try (Writer writer = new FileWriter(fileName)) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				writer.append(entry.getKey())
						.append(',')
						.append(entry.getValue())
						.append(System.getProperty("line.separator"));
			}
			return "Success saving data of users";
		} catch (IOException e) {
			return "ERROR. Could not save data!" + e;
		}
	}
}
