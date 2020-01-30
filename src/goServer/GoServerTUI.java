package goServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GoServerTUI {

	private BufferedReader in;

	public GoServerTUI() {
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	public void showMessage(String message) {
		System.out.println(message);
	}

	public int getInt(String question) throws IOException {
		showMessage(question);
		while (true) {
			try {
				return Integer.parseInt(in.readLine());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number");
			}
		}
	}

	public String getString(String question) throws IOException {
		showMessage(question);
		return in.readLine();
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
