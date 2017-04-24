/**
 * 
 */
package com.casumo.rentacasum.api;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request to return mutliple (or just one) movies.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class BatchReturnRequest {

	@NotNull
	private Long customerId;

	@NotEmpty
	@Valid
	private Long[] movies;
	
	private Date date;

	@JsonProperty
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@JsonProperty
	public Long[] getMovies() {
		return movies;
	}

	public void setMovies(Long[] movies) {
		this.movies = movies;
	}

	@JsonProperty
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}
}
