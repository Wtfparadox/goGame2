package goExceptions;

public class ConnectionLostException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8171959466719509338L;

	public ConnectionLostException(String msg) {
		super(msg);
	}
}
