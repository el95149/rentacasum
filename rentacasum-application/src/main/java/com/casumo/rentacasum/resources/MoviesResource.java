/**
 * 
 */
package com.casumo.rentacasum.resources;

import static com.casumo.rentacasum.util.IRentACasumConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.casumo.rentacasum.api.MovieRepresentation;
import com.casumo.rentacasum.api.MoviesRepresentation;
import com.casumo.rentacasum.core.Movie;
import com.casumo.rentacasum.db.MovieDAO;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.BooleanParam;
import io.dropwizard.jersey.params.IntParam;

/**
 * REST end-points related to movie operations.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
public class MoviesResource {

	private final MovieDAO movieDAO;

	public MoviesResource(MovieDAO movieDAO) {
		this.movieDAO = movieDAO;
	}

	@GET
	@UnitOfWork
	public MoviesRepresentation findByTerm(@QueryParam("term") @NotNull String term,
			@QueryParam("onlyAvailable") @DefaultValue("false") BooleanParam onlyAvailable,
			@QueryParam("start") @DefaultValue(QUERY_START_DEFAULT) IntParam start,
			@QueryParam("rows") @DefaultValue(QUERY_ROWS_DEFAULT) IntParam rows) {

		Long count;
		List<Movie> movies;
		if (onlyAvailable.get()) {
			count = movieDAO.countAvailableByTerm(term);
		} else {
			count = movieDAO.countByTerm(term);
		}
		if (count == 0) {
			throw new NotFoundException("No movies foung matching the criteria");
		}

		if (onlyAvailable.get()) {
			movies = movieDAO.findAvailableByTerm(term, start.get(), rows.get());
		} else {
			movies = movieDAO.findByTerm(term, start.get(), rows.get());
		}

		List<MovieRepresentation> moviesList = movies.stream().map(MovieRepresentation::new)
				.collect(Collectors.toCollection(ArrayList::new));
		return new MoviesRepresentation(count, start.get(), rows.get(), moviesList);

	}

}
