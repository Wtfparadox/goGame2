package goProtocol;

import java.io.IOException;

import goExceptions.ConnectionLostException;

public interface GoClientProtocol {

	/**
	 * Client identifies itself to server. Send single line in the form of
	 * H;version;name;color
	 * 
	 * @throws ServerUnavailableException
	 * @throws IOException
	 */
	public void doHandshake(int version, String name, String color) throws ConnectionLostException, IOException;

	/**
	 * Client sends suggested move as an integer to server in the form M;move upon
	 * request of the server.
	 * 
	 * Upon receiving a move request the server responds with validity of move
	 * 
	 * @throws ServerUnavailableException
	 */
	public void doMove(String move) throws ConnectionLostException;

	/**
	 * Client quits the game and terminates connection by sending Q to the server
	 * 
	 * @throws ConnectionLostException
	 * 
	 */
	public void quitGame() throws ConnectionLostException;
}
