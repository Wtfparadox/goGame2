package goProtocol;

import goExceptions.ClientUnavailableException;

public interface GoServerProtocol {

	/**
	 * After receiving a request to play from a client respond with
	 * 
	 * @return H;version;(optional free message)
	 * @throws ClientUnavailableException
	 */
	public void respondToHandshake(String finalVersion) throws ClientUnavailableException;

	/**
	 * When two clients have connected the server starts the game. Both clients
	 * receive a notification: G;board;color
	 * 
	 * @throws ClientUnavailableException
	 */
	public void startGame(char color, String board) throws ClientUnavailableException;

	/**
	 * Give turn to client in the form: T;board;opponentsLastMove, opponentsLastMove
	 * is either pass or null at start of game
	 * 
	 * @throws ClientUnavailableException
	 */
	public void giveTurnToMove(String board, String lastMove) throws ClientUnavailableException;

	/**
	 * After receiving move from client this response is sent if the move was valid
	 * 
	 * @return R;V;board
	 * @throws ClientUnavailableException
	 */
	public void validMove(String board) throws ClientUnavailableException;

	/**
	 * After receiving move from client this response is sent if the move was valid
	 * 
	 * @return R;I;(optional free message)
	 * @throws ClientUnavailableException
	 */
	public void invalidMove(String msg) throws ClientUnavailableException;

	/**
	 * The game can end for several reasons, when it does so. The server sends to
	 * clients E;reason;winner;scoreBlack;scoreWhite
	 * 
	 * Reasons can be: [F]inished, [C]heated, [D]isconnect from other client, E[X]it
	 * from other client.
	 * 
	 * @throws ClientUnavailableException
	 * 
	 */
	public void endGame(char reason, char winner, double scoreBlack, double scoreWhite)
			throws ClientUnavailableException;

}
