package goProtocol;

public interface GoClientProtocol {

	/**
	 * Send single line in the form of H;version;name;color
	 */
	public void doHandshake();

	/**
	 * Client sends suggested move as an integer to server in the form M;move upon
	 * request of the server.
	 * 
	 * Upon receiving a move request the server responds with validity of move
	 */
	public String doMove(int move);

	/**
	 * Client quits the game and terminates connection by sending Q to the server
	 * 
	 */
	public void quitGame();
}
