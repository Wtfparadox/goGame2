package goClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import goExceptions.ConnectionLostException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;

public class GoClient {

	private GoClientTUI tui;

	private InputHandlerClient ih;

	private InputStream in;
	private OutputStream out;

	private Socket sock;

	public GoClient() {
		tui = new GoClientTUI(this);
	}

	public void start() {
		boolean newGame = true;
		while (newGame) {
			try {
				createConnection();
				initializeCommunicationChannels();
				ih.makeHandShake();

				while (true) {
					ih.processInput();
				}
			} catch (ExitProgram e) {
				System.out.println("The client has stopped program execution");
				newGame = false;
			} catch (EndOfGameException | ConnectionLostException e) {
				System.out.println(e.getMessage());
				if (!tui.getBoolean("Do you wish to play another game?")) {
					System.out.println("reached");
					newGame = false;
					closeConnection();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("Please close the GUI to finish execution");
			}
		}
	}

	private void createConnection() throws ExitProgram {
		clearConnection();

		InetAddress host = null;
		int port = -1;
		while (sock == null) {
			try {
				host = tui.getIp();
				port = tui.getInt("Please enter a port number.");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Client could not read user input");
			}

			try {
				System.out.println("Establishing connection on " + host + ":" + port);
				sock = new Socket(host, port);
				in = sock.getInputStream();
				out = sock.getOutputStream();
				System.out.println("Connection established");
			} catch (IOException e) {
				System.out.println("Failed to establish connection on " + host + ":" + port);

				if (!tui.getBoolean("Do you wish to try again?")) {
					closeConnection();
					throw new ExitProgram("User has exited the program.");
				}
			}
		}
	}

	private void clearConnection() {
		in = null;
		out = null;
		sock = null;
	}

	private void initializeCommunicationChannels() throws ConnectionLostException {
		try {
			ih = new InputHandlerClient(tui, out);
			ih.startReader(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeConnection() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] s) {
		GoClient client = new GoClient();
		client.start();
	}

}
