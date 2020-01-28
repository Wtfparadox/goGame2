package goServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import goController.GameController;
import goExceptions.ExitProgram;

public class GoServer implements Runnable {

	private ServerSocket ssock;

	private GoServerTUI tui;

	private List<GameController> gameList;
	private int connectedClients;

	public GoServer(GoServerTUI tuiArg) {
		tui = tuiArg;
		gameList = new ArrayList<>();
	}

	@Override
	public void run() {
		boolean openNewSocket = true;
		while (openNewSocket) {
			try {
				setUp();

				while (true) {
					Socket sock = ssock.accept();
					System.out.println("A client has connected");
					connectedClients++;
					initiateGameLobby(sock);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setUp() throws ExitProgram, IOException {

		ssock = null;
		while (ssock == null) {
			int port = tui.getInt("Please enter the server port.");

			// try to open a new ServerSocket
			try {
				tui.showMessage("Attempting to open a socket at 127.0.0.1 " + "on port " + port + "...");
				ssock = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
				tui.showMessage("Server started at port " + port);
			} catch (IOException e) {
				tui.showMessage("ERROR: could not create a socket on " + "127.0.0.1" + " and port " + port + ".");

				if (!tui.getBoolean("Do you want to try again?")) {
					throw new ExitProgram("User indicated to exit the " + "program.");
				}
			}
		}
	}

	private void initiateGameLobby(Socket sock) throws IOException {
		int index = connectedClients;
		if (index % 2 != 0) {
			gameList.add(new GameController());
			index--;
		} else {
			index = index - 2;
		}
		GoClientHandler handler = new GoClientHandler(sock, gameList.get(index));
		new Thread(handler).start();
	}

	public void removeGame(GameController game) {
		gameList.remove(game);
	}

	public static void main(String[] s) {
		GoServer server = new GoServer(new GoServerTUI());
		new Thread(server).start();
	}
}
