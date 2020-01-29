package goClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public class PlayerReader extends Reader {

	private static final int READERSERIAL = 2832432;

	public PlayerReader(InputStream in, Queue<String> queue) {
		super(in, queue);
	}

	@Override
	public void run() {
		while (true) {
			queue.add(readMessage());
		}
	}

	public String readMessage() {
		String incomingMessage = "";
		try {
			incomingMessage = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		if (testInteger(incomingMessage) || incomingMessage.contentEquals(Character.toString(ProtocolMessages.PASS))) {
//			incomingMessage = moveFormatter(incomingMessage);
//		}
		return incomingMessage;
	}

	public void readAndAddMessage() {
		queue.add(readMessage());
	}

	public int readInteger() {
		String incomingMessage = "";
		try {
			incomingMessage = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		if (testInteger(incomingMessage) || incomingMessage.contentEquals(Character.toString(ProtocolMessages.PASS))) {
//			incomingMessage = moveFormatter(incomingMessage);
//		}
		return Integer.parseInt(incomingMessage);
	}

	private boolean testInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private String moveFormatter(String input) {
		StringBuilder builder = new StringBuilder("O;" + READERSERIAL + ";");
		builder.append(input);
		return builder.toString();
	}

	public int getSerial() {
		return READERSERIAL;
	}

}
