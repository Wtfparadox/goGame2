package goServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

import goClient.ClientReader;
import goController.GameController;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goExceptions.ServerUnavailableException;
import goGame.Player;
import goProtocol.ProtocolMessages;

public class InputHandlerServer {
	// private GoClientTUI tui;
	private LinkedBlockingQueue<String> queue;
	private GameController gameController;
	private GoClientHandler clientHandler;

	private Player player;

	public InputHandlerServer(GameController gc, GoClientHandler gch) { // GoClientTUI tuiArg
		gameController = gc;
		clientHandler = gch;
		queue = new LinkedBlockingQueue<>();
	}

	private void handleInput(String input)
			throws ExitProgram, EndOfGameException, ServerUnavailableException, IOException {
		String[] inputArguments = input.split(ProtocolMessages.DELIMITER);
		if (inputArguments.length > 0) {
			switch (input.charAt(0)) {
			case ProtocolMessages.HANDSHAKE:
				System.out.println("Handshake recieved");
				gameController.addInputHandlerServer(this);
				if (gameController.startGame()) {
					clientHandler.sendToClient(clientHandler.startGame(player.getColor().toString().charAt(0),
							gameController.boardToString()));
				}
				break;
			case ProtocolMessages.QUIT:
				System.out.println("Quit received");
				break;
			case ProtocolMessages.MOVE:
				System.out.println("Move received");
				break;
			}
		} else {
			System.out.println("Unkown command");
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void startReader(InputStream in) throws IOException {
		new Thread(new ClientReader(in, queue)).start();
	}

	public void processInput() throws ExitProgram, EndOfGameException, ServerUnavailableException, IOException {
		try {
			handleInput(queue.take());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
