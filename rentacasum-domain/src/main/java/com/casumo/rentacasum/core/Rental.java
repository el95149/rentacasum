/**
 * 
 */
package com.casumo.rentacasum.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.casumo.rentacasum.core.Customer;
import javax.persistence.ManyToOne;
import com.casumo.rentacasum.core.Movie;

/**
 * Represents a rental/return operation for one movie.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
@Entity
@Table(name = "rental")
public class Rental extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2247229977946837477L;

	/**
	 * The date the movie leaves the store.
	 */
	private Date rentalDate;

	/**
	 * The date the customer says he/she will return the movie on.
	 */
	private Date declaredReturnDate;

	/**
	 * The date on which the movie is eventually returned.
	 */
	private Date actualReturnDate;

	/**
	 * The charge the customer pays upon rental.
	 */
	private Float initialCharge;

	/**
	 * Any extra charges the customer pays upon (delayed) return.
	 */
	private Float extraCharge;

	/**
	 * The bonus points earned for renting the movie associated with this
	 * instance.
	 */
	private Integer bonusPoints;

	@ManyToOne
	private Customer customer;

	@ManyToOne
	private Movie movie;

	public Date getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(Date rentalDate) {
		this.rentalDate = rentalDate;
	}

	public Date getDeclaredReturnDate() {
		return declaredReturnDate;
	}

	public void setDeclaredReturnDate(Date declaredReturnDate) {
		this.declaredReturnDate = declaredReturnDate;
	}

	public Date getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(Date actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public Float getInitialCharge() {
		return initialCharge;
	}

	public void setInitialCharge(Float initialCharge) {
		this.initialCharge = initialCharge;
	}

	public Float getExtraCharge() {
		return extraCharge;
	}

	public void setExtraCharge(Float extraCharge) {
		this.extraCharge = extraCharge;
	}

	public Integer getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(Integer bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer param) {
		this.customer = param;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie param) {
		this.movie = param;
	}

}
