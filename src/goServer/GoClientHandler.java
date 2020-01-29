package goServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import goExceptions.ConnectionLostException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;
import goGame.ServerGame;

public class GoClientHandler implements Runnable {

	private InputHandlerServer ihs;
	private InputStream in;

	public GoClientHandler(InputStream in, OutputStream out, ServerGame gc) throws IOException {
		ihs = new InputHandlerServer(gc, out);
		this.in = in;
	}

	@Override
	public void run() {
		boolean isRunning = true;
		initializeReaders();
		while (isRunning) {
			try {
				ihs.processInput();
			} catch (ExitProgram | EndOfGameException e) {
				isRunning = false;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void initializeReaders() {
		try {
			ihs.startReader(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
