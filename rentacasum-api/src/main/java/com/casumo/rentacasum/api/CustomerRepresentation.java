/**
 * 
 */
package com.casumo.rentacasum.api;

import java.util.Date;

import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.core.Customer.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wraps around a {@link Customer} instance, providing a JSON-friendly
 * representation.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class CustomerRepresentation {

	private final Customer customer;

	public CustomerRepresentation(Customer customer) {
		super();
		this.customer = customer;
	}
	
	@JsonProperty
	public Long getId() {
		return this.customer.getId();
	}

	@JsonProperty
	public Date getBirthday() {
		return customer.getBirthday();
	}

	@JsonProperty
	public String getComments() {
		return customer.getComments();
	}

	@JsonProperty
	public String getEmail() {
		return customer.getEmail();
	}

	@JsonProperty
	public String getFirstname() {
		return customer.getFirstname();
	}

	@JsonProperty
	public Gender getGender() {
		return customer.getGender();
	}

	@JsonProperty
	public String getLastname() {
		return customer.getLastname();
	}

	@JsonProperty
	public String getOrganization() {
		return customer.getOrganization();
	}

	@JsonProperty
	public Integer getTotalBonusPoints() {
		return customer.getTotalBonusPoints();
	}
	
}
