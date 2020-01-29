package goClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.ServerUnavailableException;

public class GoClient {

	private GoClientTUI tui;

	private InputHandlerClient ih;
	private ClientWriter writer;

	private InputStream in;
	private OutputStream out;

	private Socket sock;

	public GoClient() {
		tui = new GoClientTUI(this);
		ih = new InputHandlerClient(tui);
	}

	public void start() {
		boolean newGame = true;
		while (newGame) {
			try {
				createConnection();
				initializeWriter();
				initializeReaders();
				makeHandShake();

				while (true) {
					ih.processInput();
				}
			} catch (ExitProgram e) {
				System.out.println("The client has stopped program execution");
			} catch (ServerUnavailableException e) {
				System.out.println("Could not write to server");
			} catch (EndOfGameException e) {
				tui.getBoolean("Do you wish to play another game?");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	private void makeHandShake() throws ServerUnavailableException, IOException {
		int version = tui.getInt("Which protocol version do you prefer?");
		String name = tui.getString("What is your name?");
		String color = tui.getColor();
		writer.doHandshake(version, name, color);
	}

	private void initializeWriter() throws IOException {
		writer = new ClientWriter(out);
		ih.addWriter(writer);
	}

	private void initializeReaders() {
		try {
			// ih.startReader(System.in);
			ih.startReader(new ClientReader(in, ih.getQueue()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
