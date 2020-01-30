package goServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import goExceptions.ConnectionLostException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goGame.ServerGame;
import goProtocol.ProtocolMessages;

public class GoClientHandler implements Runnable {

	private InputHandlerServer inputHandler;
	private InputStream in;
	private OutputStream out;
	private ServerGame game;
	private GoServer server;

	public GoClientHandler(Socket sock, ServerGame gc, GoServer server) throws IOException {
		this.in = sock.getInputStream();
		this.out = sock.getOutputStream();
		inputHandler = new InputHandlerServer(gc, out);
		game = gc;
		this.server = server;
	}

	@Override
	public void run() {
		boolean isRunning = true;
		initializeReaders();
		try {
			while (isRunning) {
				inputHandler.processInput();
			}
		} catch (ExitProgram e) {
			game.gameHasEnded(ProtocolMessages.EXIT);
		} catch (EndOfGameException e) {
			if (e.getMessage().contains("cheat")) {
				game.gameHasEnded(ProtocolMessages.CHEAT);
			} else {
				game.gameHasEnded(ProtocolMessages.FINISHED);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConnectionLostException e) {
			game.gameHasEnded(ProtocolMessages.DISCONNECT);
		} finally {
			try {
				server.removeGame(game);
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void initializeReaders() {
		try {
			inputHandler.startReader(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void addListener(ClientWatcher listener) {
//		listeners.add(listener);
//	}
//
//	public void removeListener(ClientWatcher listener) {
//		listeners.remove(listener);
//	}
//
//	public void notifyAllListeners(char reason, int number) {
//		for (ClientWatcher run : listeners) {
//			run.notifyRunnableEnd(this, reason, number);
//		}
//	}
}
