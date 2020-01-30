package goServerClientCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

import goClient.ClientReader;
import goExceptions.ConnectionLostException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goPlayers.Player;

public abstract class InputHandler {

	protected LinkedBlockingQueue<String> queue;
	protected Player player;

	public InputHandler() {
		queue = new LinkedBlockingQueue<>();
	}

	protected abstract void handleInput(String input)
			throws ExitProgram, EndOfGameException, ConnectionLostException, IOException;

	public void startReader(InputStream in) throws IOException {
		new Thread(new ClientReader(in, queue)).start();
	}

	public void processInput() throws ExitProgram, EndOfGameException, IOException, ConnectionLostException {
		try {
			handleInput(queue.take());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void closeInputHandler(InputHandler handler) {

	}

}
