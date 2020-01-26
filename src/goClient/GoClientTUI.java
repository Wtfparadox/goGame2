package goClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
		return Integer.parseInt(in.readLine());
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
