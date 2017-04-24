/**
 * 
 */
package com.casumo.rentacasum.service;

import com.casumo.rentacasum.api.BatchRentalRequest;
import com.casumo.rentacasum.api.BatchReturnRequest;
import com.casumo.rentacasum.api.RentalResponse;
import com.casumo.rentacasum.api.ReturnResponse;

/**
 * Rental operations business logic should be defined by implementing classes.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public interface IRentalService {

	/**
	 * Rent a batch of movies.
	 * 
	 * @param batchRentalRequest
	 *            The {@link BatchRentalRequest} holding the desired movies'
	 *            renting details.
	 * @return A {@link RentalResponse} containing rental details.
	 */
	RentalResponse rentMovies(BatchRentalRequest batchRentalRequest);

	/**
	 * Return a batch of rented movies.
	 * 
	 * @param batchReturnRequest
	 *            The {@link BatchReturnRequest} holding the returned movies
	 *            details.
	 * @return a {@link ReturnResponse} containing return details (e.g. any
	 *         extra charges).
	 */
	ReturnResponse returnMovies(BatchReturnRequest batchReturnRequest);

}
