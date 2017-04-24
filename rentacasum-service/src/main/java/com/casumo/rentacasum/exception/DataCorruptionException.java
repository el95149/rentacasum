/**
 * 
 */
package com.casumo.rentacasum.exception;

/**
 * An error indicating that something in the database is not right any more!
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class DataCorruptionException extends RentACasumException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3953996822965734631L;

	public DataCorruptionException() {
		super();
	}

	public DataCorruptionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DataCorruptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataCorruptionException(String message) {
		super(message);
	}

	public DataCorruptionException(Throwable cause) {
		super(cause);
	}

}
