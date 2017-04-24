/**
 * 
 */
package com.casumo.rentacasum.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.casumo.rentacasum.core.Movie;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * Movie entity related DAO.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class MovieDAO extends AbstractDAO<Movie> {

	public MovieDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<Movie> findById(Long id) {
		return Optional.ofNullable(get(id));
	}

	/**
	 * Fetch all movies.
	 * 
	 * @return a list of movies, or an empty list if no resuts found.
	 */
	public List<Movie> findAll() {
		return list(namedQuery("findAllMovies"));
	}

	/**
	 * Searches movies using a textual term ('contains' style like search) on
	 * various {@link Movie} fields
	 * 
	 * @param term
	 *            The term to search for.
	 * @return a row count of movies.
	 */
	public Long countByTerm(String term) {
		Query<Long> query = currentSession().createQuery(
				"select count(*) from Movie m where m.title like :term or m.description like :term or m.imdbCode like :term", Long.class);
		query.setParameter("term", "%" + term + "%");
		return query.uniqueResult();
	}

	/**
	 * Searches movies using a textual term ('contains' style like search) on
	 * various {@link Movie} fields.
	 * 
	 * @param term
	 *            The term to search for.
	 * @param startPosition
	 *            starting row.
	 * @param maxResult
	 *            maximum number of rows to fetch.
	 * @return a list of movies, or an empty list if no resuts found.
	 */
	public List<Movie> findByTerm(String term, int startPosition, int maxResult) {
		Query<Movie> query = query(
				"from Movie m where m.title like :term or m.description like :term or m.imdbCode like :term");
		query.setParameter("term", "%" + term + "%");
		query.setFirstResult(startPosition);
		query.setMaxResults(maxResult);
		return list(query);
	}

	/**
	 * Searches available (not rented) movies using a textual term ('contains'
	 * style like search) on various {@link Movie} fields.
	 * 
	 * @param term
	 *            The term to search for.
	 * @return a row count of available movies.
	 */
	public Long countAvailableByTerm(String term) {
		Query<Long> query = currentSession()
				.createQuery(
						"select count(*) from Movie m where (m.title like :term or m.description like :term or m.imdbCode like :term)"
								+ " and m not in (select r.movie from Rental r where r.actualReturnDate is null)",
						Long.class);
		query.setParameter("term", "%" + term + "%");
		return query.uniqueResult();
	}

	/**
	 * Searches available (not rented) movies using a textual term ('contains'
	 * style like search) on various {@link Movie} fields
	 * 
	 * @param term
	 *            The term to search for.
	 * @param startPosition
	 *            starting row.
	 * @param maxResult
	 *            maximum number of rows to fetch.
	 * @return a list of available movies, or an empty list if no resuts found.
	 */
	public List<Movie> findAvailableByTerm(String term, int startPosition, int maxResult) {
		Query<Movie> query = query(
				"from Movie m where (m.title like :term or m.description like :term or m.imdbCode like :term)"
						+ " and m not in (select r.movie from Rental r where r.actualReturnDate is null)");
		query.setParameter("term", "%" + term + "%");
		query.setFirstResult(startPosition);
		query.setMaxResults(maxResult);
		return list(query);
	}

	/**
	 * Creates or updates a Movie.
	 * 
	 * @param movie
	 *            The {@link Movie} to persist
	 * @return The new or updated instance.
	 */
	public Movie save(Movie movie) {
		return persist(movie);
	}

}
