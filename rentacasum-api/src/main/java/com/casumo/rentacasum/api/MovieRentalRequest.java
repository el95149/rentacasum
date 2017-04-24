/**
 * 
 */
package com.casumo.rentacasum.api;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class MovieRentalRequest {

	@NotNull
	private Long movie;

	@NotNull
	@Min(1L)
	private Integer days;

	@JsonProperty
	public Long getMovie() {
		return movie;
	}

	public void setMovie(Long movie) {
		this.movie = movie;
	}

	@JsonProperty
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,ToStringStyle.JSON_STYLE);
	}
	
}
