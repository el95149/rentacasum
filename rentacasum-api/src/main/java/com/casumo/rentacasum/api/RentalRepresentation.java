/**
 * 
 */
package com.casumo.rentacasum.api;

import java.util.Date;

import com.casumo.rentacasum.core.Rental;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class RentalRepresentation {

	private final Rental rental;

	public RentalRepresentation(Rental rental) {
		super();
		this.rental = rental;
	}

	@JsonProperty
	public Long getCustomerId() {
		return this.rental.getCustomer().getId();
	}

	@JsonProperty
	public Long getMovieId() {
		return this.rental.getMovie().getId();
	}

	@JsonProperty
	public Date getRentalDate() {
		return this.rental.getRentalDate();
	}

	@JsonProperty
	public Date getDeclaredReturnDate() {
		return this.rental.getDeclaredReturnDate();
	}

	@JsonProperty
	public Date getActualReturnDate() {
		return this.rental.getActualReturnDate();
	}

	@JsonProperty
	public Float getInitialCharge() {
		return this.rental.getInitialCharge();
	}
	
	@JsonProperty
	public Float getExtraCharge() {
		return this.rental.getExtraCharge();
	}
	
	@JsonProperty
	public Integer getBonusPoints() {
		return this.rental.getBonusPoints();
	}

}
