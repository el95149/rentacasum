/**
 * 
 */
package com.casumo.rentacasum.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.core.Movie;
import com.casumo.rentacasum.core.Rental;
import com.casumo.rentacasum.exception.DataCorruptionException;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * Rental entity related DAO.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class RentalDAO extends AbstractDAO<Rental> {

	public RentalDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<Rental> findById(Long id) {
		return Optional.ofNullable(get(id));
	}
	
	/**
	 * Find a movie rental that has not been returned yet.
	 * 
	 * @param movie
	 *            The movie to check rentals for.
	 * @return An Optional active Rental instance.
	 * @throws DataCorruptionException
	 *             if more than a single active rental is found.
	 */
	public Optional<Rental> findActiveRental(Movie movie) {
		Query<Rental> query = query("from Rental r where r.movie = :movie  and r.actualReturnDate is null");
		query.setParameter("movie", movie);
		List<Rental> rentals = list(query);
		if (rentals.size() > 1) {
			throw new DataCorruptionException("More than one active rentals found for movie! id: " + movie.getId());
		}
		return rentals.isEmpty() ? Optional.empty() : Optional.of(rentals.get(0));
	}

	/**
	 * Find any rentals the customer has yet to return.
	 * 
	 * @param customer
	 *            The {@link Customer} instance to search for.
	 * @return a list of rentals the customer is still holding on to.
	 */
	public List<Rental> findActiveRentals(Customer customer) {
		Query<Rental> query = query("from Rental r where r.customer = :customer" + " and r.actualReturnDate is null");
		query.setParameter("customer", customer);
		return list(query);
	}

	/**
	 * Find a movie rental, for a specific customer, that has not been returned
	 * yet.
	 * 
	 * @param movie
	 *            The {@link Movie} instance to check rentals for.
	 * @param customer
	 *            the {@link Customer} instance to check for.
	 * @return An Optional active {@link Rental} instance.
	 * @throws DataCorruptionException
	 *             if more than a single active rental is found.
	 */
	public Optional<Rental> findActiveRental(Movie movie, Customer customer) {
		Query<Rental> query = query(
				"from Rental r where r.movie = :movie and r.customer = :customer" + " and r.actualReturnDate is null");
		query.setParameter("movie", movie);
		query.setParameter("customer", customer);
		List<Rental> rentals = list(query);
		if (rentals.size() > 1) {
			throw new DataCorruptionException(
					"More than one active rentals found for movie/customer! movie Id/customer Id: " + movie.getId()
							+ "/" + customer.getId());
		}
		return rentals.isEmpty() ? Optional.empty() : Optional.of(rentals.get(0));
	}

	/**
	 * Creates or updates a rental.
	 * 
	 * @param rental
	 *            The {@link Rental} to persist
	 * @return The new or updated instance.
	 */
	public Rental save(Rental rental) {
		return persist(rental);
	}

}
