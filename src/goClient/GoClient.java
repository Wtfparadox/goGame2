package goClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.ServerUnavailableException;
import goProtocol.GoClientProtocol;
import goProtocol.ProtocolMessages;

public class GoClient implements GoClientProtocol {

	private GoClientTUI tui;
	private InputHandlerClient ih;

	private Socket sock;

	private BufferedReader in;
	private BufferedWriter out;

	public GoClient(GoClientTUI tuiArg) {
		tui = tuiArg;
		ih = new InputHandlerClient(tui);
	}

	public void start() throws IOException {
		boolean newGame = true;
		while (newGame) {
			try {
				createConnection();
				initializeReaders();
				doHandshake();
				ih.processInput();
			} catch (ExitProgram e) {
				System.out.println("The client has stopped program execution");
			} catch (ServerUnavailableException e) {
				System.out.println("Could not write to server");
			} catch (EndOfGameException e) {
				tui.getBoolean("Do you wish to play another game?");
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
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
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

	private void initializeReaders() {
		try {
			ih.startReader(System.in);
			ih.startReader(sock.getInputStream());
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

//	public String readLineFromServer() throws ServerUnavailableException {
//		if (in != null) {
//			try {
//				String answer = in.readLine();
//				if (answer == null) {
//					throw new ServerUnavailableException("Could not read " + "from server.");
//				}
//				return answer;
//			} catch (IOException e) {
//				throw new ServerUnavailableException("Could not read " + "from server.");
//			}
//		} else {
//			throw new ServerUnavailableException("Could not read " + "from server.");
//		}
//	}

	public void sendMessage(String msg) throws ServerUnavailableException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				throw new ServerUnavailableException("Could not write to server.");
			}
		} else {
			throw new ServerUnavailableException("Could not write to server.");
		}
	}

	@Override
	public void doHandshake() throws ServerUnavailableException, IOException {
		int version = tui.getInt("Which protocol version do you prefer?");
		String name = tui.getString("What is your name?");
		String messageForServer = ProtocolMessages.HANDSHAKE + ProtocolMessages.DELIMITER + version
				+ ProtocolMessages.DELIMITER + name;
		if (tui.getBoolean("Do you wish to choose a color?")) {
			String color = tui.getColor();
			messageForServer = messageForServer + ProtocolMessages.DELIMITER + color;
		}
		sendMessage(messageForServer);
	}

	@Override
	public String doMove(int move) {
		return ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + move;
	}

	@Override
	public void quitGame() throws ServerUnavailableException {
		sendMessage(Character.toString(ProtocolMessages.QUIT));
	}

}
