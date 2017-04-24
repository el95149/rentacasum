/**
 * 
 */
package com.casumo.rentacasum.exception;

import com.casumo.rentacasum.core.Movie;

/**
 * Exception specific to customer {@link Movie} entity business logic.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class MovieNotFoundException extends RentACasumException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2898583089990618312L;

	private final Long movieId;

	public MovieNotFoundException(Long movieId) {
		super();
		this.movieId = movieId;
	}

	public MovieNotFoundException(Long movieId, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.movieId = movieId;
	}

	public MovieNotFoundException(Long movieId, String message, Throwable cause) {
		super(message, cause);
		this.movieId = movieId;
	}

	public MovieNotFoundException(Long movieId, String message) {
		super(message);
		this.movieId = movieId;
	}

	public MovieNotFoundException(Long movieId, Throwable cause) {
		super(cause);
		this.movieId = movieId;
	}

	public Long getMovieId() {
		return movieId;
	}
	
}
