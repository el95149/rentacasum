/**
 * 
 */
package com.casumo.rentacasum.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class BatchRentalRequest {

	@NotNull
	private Long customerId;

	@NotEmpty
	@Valid
	private MovieRentalRequest[] movies;

	@JsonProperty
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@JsonProperty
	public MovieRentalRequest[] getMovies() {
		return movies;
	}

	public void setMovies(MovieRentalRequest[] movies) {
		this.movies = movies;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}
}
