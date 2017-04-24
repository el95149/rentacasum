/**
 * 
 */
package com.casumo.rentacasum.exception;

import com.casumo.rentacasum.core.Rental;

/**
 * Exception specific to customer {@link Rental} entity business logic.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class RentalNotFoundException extends RentACasumException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2898583089990618312L;

	private final Long movieId;
	
	private final Long customerId;

	public RentalNotFoundException(Long movieId, Long customerId) {
		super();
		this.movieId = movieId;
		this.customerId = customerId;
	}

	public RentalNotFoundException(Long movieId, Long customerId, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.movieId = movieId;
		this.customerId = customerId;
	}

	public RentalNotFoundException(Long movieId, Long customerId, String message, Throwable cause) {
		super(message, cause);
		this.movieId = movieId;
		this.customerId = customerId;
	}

	public RentalNotFoundException(Long movieId, Long customerId, String message) {
		super(message);
		this.movieId = movieId;
		this.customerId = customerId;
	}

	public RentalNotFoundException(Long movieId, Long customerId, Throwable cause) {
		super(cause);
		this.movieId = movieId;
		this.customerId = customerId;
	}

	public Long getMovieId() {
		return movieId;
	}

	public Long getCustomerId() {
		return customerId;
	}
	
}
