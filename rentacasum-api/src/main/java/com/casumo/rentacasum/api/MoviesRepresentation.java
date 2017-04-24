/**
 * 
 */
package com.casumo.rentacasum.api;

import java.util.List;

import com.casumo.rentacasum.core.Movie;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains {@link Movie} instances, providing a JSON-friendly representation.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class MoviesRepresentation {

	private final Long totalRows;
	
	private final Integer start;
	
	private final Integer rows;

	private final List<MovieRepresentation> movies;

	public MoviesRepresentation(Long totalRows, Integer start, Integer rows, List<MovieRepresentation> movies) {
		super();
		this.totalRows = totalRows;
		this.start = start;
		this.rows =rows;
		this.movies = movies;
	}

	@JsonProperty
	public Long getTotalRows() {
		return totalRows;
	}

	@JsonProperty
	public Integer getStart() {
		return start;
	}

	@JsonProperty
	public Integer getRows() {
		return rows;
	}

	@JsonProperty
	public List<MovieRepresentation> getMovies() {
		return movies;
	}

}
