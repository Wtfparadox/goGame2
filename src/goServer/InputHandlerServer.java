package goServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import goClient.ClientReader;
import goController.GameController;
import goExceptions.ClientUnavailableException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goGame.Player;
import goProtocol.ProtocolMessages;

public class InputHandlerServer {
	// private GoClientTUI tui;
	private LinkedBlockingQueue<String> queue;
	private GameController controller;
	private ServerWriter writer;

	private Player player;

	public InputHandlerServer(GameController gc, OutputStream out) { // GoClientTUI tuiArg
		controller = gc;
		writer = new ServerWriter(out);
		queue = new LinkedBlockingQueue<>();
	}

	private void handleInput(String input)
			throws ExitProgram, EndOfGameException, IOException, ClientUnavailableException {
		String[] inputArguments = input.split(ProtocolMessages.DELIMITER);
		if (inputArguments.length > 0) {
			switch (input.charAt(0)) {
			case ProtocolMessages.HANDSHAKE:
				System.out.println("Handshake recieved");
				writer.respondToHandshake("1");
				controller.addInputHandler(this);
				controller.readyForGame();
				break;
			case ProtocolMessages.QUIT:
				System.out.println("Quit received");
				break;
			case ProtocolMessages.MOVE:
				System.out.println("Move received");
				int move = Integer.parseInt(inputArguments[1]);
				handleMove(move);
				break;
			}
		} else {
			System.out.println("Unkown command");
		}
	}

	public void handleMove(int move) throws IOException, EndOfGameException, ClientUnavailableException {
		if (controller.moveHandler(player.getColor(), move)) {
			writer.validMove(controller.boardToString());
		} else {
			writer.invalidMove("Your move is invalid");
			throw new EndOfGameException("Cheated");
		}
	}

	public void notifyTurn(String lastMove) throws ClientUnavailableException {
		writer.giveTurnToMove(controller.boardToString(), lastMove);
	}

	public void beginGame() throws ClientUnavailableException {
		writer.startGame(player.getColor().toString().charAt(0), controller.boardToString());
	}

	public void startReader(InputStream in) throws IOException {
		new Thread(new ClientReader(in, queue)).start();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void processInput() throws ExitProgram, EndOfGameException, IOException, ClientUnavailableException {
		try {
			handleInput(queue.take());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
