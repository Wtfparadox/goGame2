package goServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import goController.Game;

public class GoServer {

	private ServerSocket ssock;

	private GoServerTUI tui;

	private List<Game> games;

	public GoServer(GoServerTUI tuiArg) {
		tui = tuiArg;
	}

	private void setUp() {

		try {
			int port = tui.getInt("Please provide a port number to listen on");
			ssock = new ServerSocket(port);
		} catch (IOException ioe) {

		}
	}
}
