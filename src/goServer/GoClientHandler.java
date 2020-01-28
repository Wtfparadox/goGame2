package goServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import goController.GameController;
import goExceptions.ClientUnavailableException;
import goExceptions.EndOfGameException;
import goExceptions.ExitProgram;

public class GoClientHandler implements Runnable {

	private InputHandlerServer ihs;
	private InputStream in;

	public GoClientHandler(InputStream in, OutputStream out, GameController gc) throws IOException {
		ihs = new InputHandlerServer(gc, out);
		this.in = in;
	}

	@Override
	public void run() {
		boolean isRunning = true;
		while (isRunning) {
			initializeReaders();
			try {
				ihs.processInput();
			} catch (ExitProgram | EndOfGameException e) {
				isRunning = false;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientUnavailableException e) {
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
