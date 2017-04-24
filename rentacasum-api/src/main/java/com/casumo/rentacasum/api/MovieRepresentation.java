/**
 * 
 */
package com.casumo.rentacasum.api;

import java.util.Date;

import com.casumo.rentacasum.core.Movie;
import com.casumo.rentacasum.core.Movie.Type;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wraps around a {@link Movie} instance, providing a JSON-friendly representation. 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class MovieRepresentation {

	private final Movie movie;

	public MovieRepresentation(Movie movie) {
		super();
		this.movie = movie;
	}

	@JsonProperty
	public Long getId() {
		return this.movie.getId();
	}
	
	@JsonProperty
	public String getTitle() {
		return this.movie.getTitle();
	}

	@JsonProperty
	public String getDescription() {
		return this.movie.getDescription();
	}

	@JsonProperty
	public Type getType() {
		return this.movie.getType();
	}
	
	@JsonProperty
	public Date getReleaseDate() {
		return this.movie.getReleaseDate();
	}
	
}
