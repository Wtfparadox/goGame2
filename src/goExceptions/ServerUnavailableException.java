package goExceptions;

public class ServerUnavailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5541473526199518045L;

	public ServerUnavailableException(String msg) {
		super(msg);
	}
}
