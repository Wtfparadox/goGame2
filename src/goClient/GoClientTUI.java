package goClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Queue;

import goPlayers.ComputerPlayer;
import goPlayers.FreeFieldStrategy;
import goPlayers.HumanPlayer;
import goPlayers.Player;

public class GoClientTUI {
	private BufferedReader in;

	public GoClientTUI(GoClient gcArg) {
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	public void showMessage(String message) {
		System.out.println(message);
	}

	public InetAddress getIp() throws IOException {
		System.out.println("Please provide a valid IP");
		InetAddress ip = null;
		while (ip == null) {
			try {
				String inputIP = in.readLine();
				ip = InetAddress.getByName(inputIP);
			} catch (UnknownHostException u) {
				System.out.println("This IP is invalid. Please provide a valid IP.");
			}
		}
		return ip;
	}

	public String getString(String question) throws IOException {
		showMessage(question);
		return in.readLine();
	}

	public int getInt(String question) throws IOException {
		showMessage(question);
		while (true) {
			try {
				return Integer.parseInt(in.readLine());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid integer");
			}
		}
	}

	public String getColor() throws IOException {
		boolean isNotColor = true;
		String color = "";
		while (isNotColor) {
			color = getString("Please choose a color: Enter W for white or B for black");
			if (color.equalsIgnoreCase("w") || color.equalsIgnoreCase("b")) {
				isNotColor = false;
			}
		}
		return color;
	}

	public Player choosePlayer(Queue<String> queue, ClientWriter writer) throws IOException {
		String input = getString("Choose a player type: [C]omputer or [H]uman");
		while (!(input.contentEquals("C")) && !(input.contentEquals("H"))) {
			System.out.println("Invalid choice, please choose: [C]omputer or [H]uman");
			input = in.readLine();
		}
		if (input.contentEquals("C")) {
			System.out.println("You chose: Computer Player");
			return new ComputerPlayer("Thomas", new FreeFieldStrategy(), queue, writer);
		} else {
			System.out.println("You chose: Human Player");
			return new HumanPlayer("Thomas", queue, writer);
		}
	}

	public boolean getBoolean(String question) {
		showMessage(question);
		String input = "";
		while (!(input.contentEquals("yes")) && !(input.contentEquals("no"))) {
			try {
				System.out.println("Please answer yes or no");
				input = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (input.contentEquals("yes")) {
			return true;
		} else {
			return false;
		}
	}
}
