/**
 * 
 */
package com.casumo.rentacasum.exception;

import com.casumo.rentacasum.core.Customer;

/**
 * Exception specific to customer {@link Customer} entity business logic.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class CustomerNotFoundException extends RentACasumException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5785760165961511924L;

	private final Long customerId;

	public CustomerNotFoundException(Long customerId) {
		super();
		this.customerId = customerId;
	}

	public CustomerNotFoundException(Long customerId, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.customerId = customerId;
	}

	public CustomerNotFoundException(Long customerId, String message, Throwable cause) {
		super(message, cause);
		this.customerId = customerId;
	}

	public CustomerNotFoundException(Long customerId, String message) {
		super(message);
		this.customerId = customerId;
	}

	public CustomerNotFoundException(Long customerId, Throwable cause) {
		super(cause);
		this.customerId = customerId;
	}

	public Long getCustomerId() {
		return customerId;
	}

}
