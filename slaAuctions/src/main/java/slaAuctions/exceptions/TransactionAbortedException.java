package slaAuctions.exceptions;

public class TransactionAbortedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5005977762454656519L;
	
	public TransactionAbortedException(String msg) {
		super(msg);
	}

}
