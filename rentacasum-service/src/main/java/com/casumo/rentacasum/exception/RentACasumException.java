/**
 * 
 */
package com.casumo.rentacasum.exception;

/**
 * Base application logic related exception class.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public abstract class RentACasumException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -817281442280595487L;

	public RentACasumException() {
		super();
	}

	public RentACasumException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RentACasumException(String message, Throwable cause) {
		super(message, cause);
	}

	public RentACasumException(String message) {
		super(message);
	}

	public RentACasumException(Throwable cause) {
		super(cause);
	}

}
