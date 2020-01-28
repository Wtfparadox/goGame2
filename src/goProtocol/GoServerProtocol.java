package goProtocol;

public interface GoServerProtocol {

	/**
	 * After receiving a request to play from a client respond with
	 * 
	 * @return H;version;(optional free message)
	 */
	public String respondToHandshake(String finalVersion);

	/**
	 * When two clients have connected the server starts the game. Both clients
	 * receive a notification: G;board;color
	 */
	public String startGame(char color, String board);

	/**
	 * Give turn to client in the form: T;board;opponentsLastMove, opponentsLastMove
	 * is either pass or null at start of game
	 */
	public String giveTurnToMove(String board, int lastMove);

	/**
	 * After receiving move from client this response is sent if the move was valid
	 * 
	 * @return R;V;board
	 */
	public String validMove(String board);

	/**
	 * After receiving move from client this response is sent if the move was valid
	 * 
	 * @return R;I;(optional free message)
	 */
	public String invalidMove(String msg);

	/**
	 * The game can end for several reasons, when it does so. The server sends to
	 * clients E;reason;winner;scoreBlack;scoreWhite
	 * 
	 * Reasons can be: [F]inished, [C]heated, [D]isconnect from other client, E[X]it
	 * from other client.
	 * 
	 */
	public String endGame(char reason, char winner, double scoreBlack, double scoreWhite);

}
