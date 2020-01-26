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

	public String getString(String question) throws IOException {
		System.out.println(question);
		return in.readLine();
	}

	public int getInt(String question) throws IOException {
		System.out.println(question);
		return Integer.parseInt(in.readLine());
	}

}
