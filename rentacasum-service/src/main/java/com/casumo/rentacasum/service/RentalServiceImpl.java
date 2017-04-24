/**
 * 
 */
package com.casumo.rentacasum.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casumo.rentacasum.api.BatchRentalRequest;
import com.casumo.rentacasum.api.BatchReturnRequest;
import com.casumo.rentacasum.api.MovieRentalRequest;
import com.casumo.rentacasum.api.RentalResponse;
import com.casumo.rentacasum.api.ReturnResponse;
import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.core.Movie;
import com.casumo.rentacasum.core.Rental;
import com.casumo.rentacasum.db.CustomerDAO;
import com.casumo.rentacasum.db.MovieDAO;
import com.casumo.rentacasum.db.RentalDAO;
import com.casumo.rentacasum.exception.CustomerNotFoundException;
import com.casumo.rentacasum.exception.DataCorruptionException;
import com.casumo.rentacasum.exception.MovieAlreadyRentedException;
import com.casumo.rentacasum.exception.MovieNotFoundException;
import com.casumo.rentacasum.exception.RentalNotFoundException;

/**
 * Concrete implementation of {@link IRentalService}.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class RentalServiceImpl implements IRentalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalServiceImpl.class);

	private Float premiumPrice;

	private Float basicPrice;

	private MovieDAO movieDAO;

	private RentalDAO rentalDAO;

	private CustomerDAO customerDAO;

	public RentalServiceImpl(MovieDAO movieDAO, RentalDAO rentalDAO, CustomerDAO customerDAO, Float premiumPrice,
			Float basicPrice) {
		this.movieDAO = movieDAO;
		this.rentalDAO = rentalDAO;
		this.customerDAO = customerDAO;
		this.premiumPrice = premiumPrice;
		this.basicPrice = basicPrice;
	}

	private Customer getCustomer(Long customerId) {
		Optional<Customer> optionalCustomer = customerDAO.findById(customerId);
		if (!optionalCustomer.isPresent()) {
			LOGGER.error("Customer not found, id: {}", customerId);
			throw new CustomerNotFoundException(customerId, "Customer not found, id:" + customerId);
		}
		return optionalCustomer.get();
	}

	private Rental calculateInitialCharge(MovieRentalRequest movieRentalRequest, Rental rental) {
		Float initialCharge = null;
		switch (rental.getMovie().getType()) {
		case NEW_RELEASE:
			initialCharge = premiumPrice * movieRentalRequest.getDays();
			break;
		case REGULAR:
			initialCharge = basicPrice;
			if (movieRentalRequest.getDays() > 3) {
				initialCharge += basicPrice * (movieRentalRequest.getDays() - 3);
			}
			break;
		case OLD:
			initialCharge = basicPrice;
			if (movieRentalRequest.getDays() > 5) {
				initialCharge += basicPrice * (movieRentalRequest.getDays() - 5);
			}
			break;
		default:
			break;
		}
		rental.setInitialCharge(initialCharge);
		return rental;
	}

	private Rental calculateBonusPoints(Rental rental) {
		switch (rental.getMovie().getType()) {
		case NEW_RELEASE:
			rental.setBonusPoints(2);
			break;
		default:
			rental.setBonusPoints(1);
			break;
		}
		return rental;
	}

	private Rental calculateExtraCharge(Rental rental) {

		LocalDate rentalLocalDate = rental.getRentalDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate actualReturnLocalDate = rental.getActualReturnDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		LocalDate declaredReturnLocalDate = rental.getDeclaredReturnDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();

		long declaredDays = ChronoUnit.DAYS.between(rentalLocalDate, declaredReturnLocalDate);
		long actualDays = ChronoUnit.DAYS.between(rentalLocalDate, actualReturnLocalDate);
		long daysBetweenDeclaredAndActualReturn = ChronoUnit.DAYS.between(declaredReturnLocalDate,
				actualReturnLocalDate);

		Float extraCharge = 0.0f;
		switch (rental.getMovie().getType()) {
		case NEW_RELEASE:
			if (daysBetweenDeclaredAndActualReturn > 0) {
				// customer returned the movie after promised, pay for any
				// additional days.
				extraCharge = premiumPrice * daysBetweenDeclaredAndActualReturn;
			}
			break;
		case REGULAR:
			if (actualDays <= 3 || actualDays <= declaredDays) {
				// customer returned the movie before the first 3 days, or
				// before, or as promised.
				// No extra charge, already paid for
				break;
			}
			// customer returned the movie after promised
			// was the movie declared for <= 3 days?
			if (declaredDays <= 3) {
				// customer has already paid for 3 days, must pay any additional
				// days above 3.
				extraCharge = basicPrice * (actualDays - 3);
			} else {
				// customer has already paid for declared days, must must pay
				// for any additional days above those.
				extraCharge = basicPrice * (actualDays - declaredDays);
			}
			break;
		case OLD:
			if (actualDays <= 5 || actualDays <= declaredDays) {
				// customer returned the movie before the first 5 days, or
				// before, or as promised.
				// No extra charge, already paid for
				break;
			}
			// customer returned the movie after promised.
			// was the movie declared for <= 5 days?
			if (declaredDays <= 5) {
				// customer has already paid for 5 days, must pay any additional
				// days above 5.
				extraCharge = basicPrice * (actualDays - 5);
			} else {
				// customer has already paid for declared days, must must pay
				// for any additional days above those.
				extraCharge = basicPrice * (actualDays - declaredDays);
			}
			break;
		default:
			break;
		}
		rental.setExtraCharge(extraCharge);
		return rental;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.casumo.rentacasum.service.IRentalService#rentMovies(com.casumo.
	 * rentacasum.api.BatchRentalRequest)
	 */
	@Override
	public RentalResponse rentMovies(BatchRentalRequest batchRentalRequest) {

		// fetch customer (if he/she exists)
		Long customerId = batchRentalRequest.getCustomerId();
		Customer customer = getCustomer(customerId);

		// create an empty (at first) rentals list
		List<Rental> rentals = new ArrayList<>();

		// hold on to a time-stamp, used to mark all rentals
		Date now = new Date();
		// truncate current date to today
		Date today = DateUtils.truncate(now, Calendar.DATE);

		// process requested movies
		MovieRentalRequest[] movieRentalRequests = batchRentalRequest.getMovies();
		for (MovieRentalRequest movieRentalRequest : movieRentalRequests) {
			// check if movie exists
			Long movieId = movieRentalRequest.getMovie();
			Optional<Movie> optionalMovie = movieDAO.findById(movieId);
			if (!optionalMovie.isPresent()) {
				LOGGER.error("Movie not found, id:{}", movieId);
				throw new MovieNotFoundException(movieId, "Movie not found, id:" + movieId);
			}
			Movie movie = optionalMovie.get();

			// check if movie is already rented
			Optional<Rental> optionalRental = rentalDAO.findActiveRental(movie);
			if (optionalRental.isPresent()) {
				LOGGER.error("Movie already rented, id:{}", movieId);
				throw new MovieAlreadyRentedException(movie.getId(), "Movie already rented, id:" + movieId);
			}

			Rental rental = new Rental();
			rental.setCustomer(customer);
			rental.setMovie(movie);
			rental.setRentalDate(today);

			// calculate declared return date
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);
			calendar.add(Calendar.DATE, movieRentalRequest.getDays());
			rental.setDeclaredReturnDate(calendar.getTime());

			rental.setCreateDate(new Timestamp(now.getTime()));

			// calculate initial rental charge
			rental = calculateInitialCharge(movieRentalRequest, rental);

			// calculate any bonus points received
			rental = calculateBonusPoints(rental);

			// create rental
			rental = rentalDAO.save(rental);

			rentals.add(rental);
		}

		// calculate total bonus points
		Integer totalBonusPoints = customer.getTotalBonusPoints() == null ? 0 : customer.getTotalBonusPoints();
		int rentalBonusPoints = rentals.stream().mapToInt(Rental::getBonusPoints).sum();
		totalBonusPoints += rentalBonusPoints;

		// update customer's total bonus points
		customer.setTotalBonusPoints(totalBonusPoints);
		customerDAO.save(customer);

		// calculate total charge
		double initialTotalCharge = rentals.stream().mapToDouble(Rental::getInitialCharge).sum();

		RentalResponse rentalResponse = new RentalResponse();
		rentalResponse.setCharge(new Float(initialTotalCharge));
		rentalResponse.setBonusPoints(rentalBonusPoints);
		rentalResponse.setTotalBonusPoints(totalBonusPoints);

		return rentalResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.casumo.rentacasum.service.IRentalService#returnMovies(com.casumo.
	 * rentacasum.api.BatchReturnRequest)
	 */
	@Override
	public ReturnResponse returnMovies(BatchReturnRequest batchReturnRequest) {

		// fetch customer (if he/she exists)
		Long customerId = batchReturnRequest.getCustomerId();
		Customer customer = getCustomer(customerId);

		// create an empty (at first) rentals list
		List<Rental> rentals = new ArrayList<>();

		// hold on to a time-stamp, used to mark all rentals
		Date now = new Date();
		// Set return date to either today, or to forced return date (used
		// mostly, for testing purposes)
		Date returnDate = DateUtils.truncate(batchReturnRequest.getDate() == null ? now : batchReturnRequest.getDate(),
				Calendar.DATE);

		// process returned movies
		Long[] movieIds = batchReturnRequest.getMovies();
		for (Long movieId : movieIds) {

			// check if rental exists and is active
			Movie movie = new Movie();
			movie.setId(movieId);
			Optional<Rental> optionalRental = rentalDAO.findActiveRental(movie, customer);
			if (!optionalRental.isPresent()) {
				LOGGER.error("Rental not found, movie id/customer id:{}/{}", movieId, customerId);
				throw new RentalNotFoundException(movieId, customerId,
						"Movie not found, movie id/customer id:" + movieId + "/" + customerId);
			}

			Rental rental = optionalRental.get();

			// check whether actual return date is before the rental date.
			// *NOTE*: Only useful when forcing return dates in the request, for
			// testing purposes
			if (returnDate.before(rental.getRentalDate())) {
				LOGGER.error("Return date is before rental date, rental Id/rental date:{}/{}", rental.getId(),
						rental.getRentalDate());
				throw new DataCorruptionException("Return date is before rental date, rental Id/rental date"
						+ rental.getId() + "/" + rental.getRentalDate());
			}

			rental.setActualReturnDate(returnDate);
			rental.setUpdateDate(new Timestamp(now.getTime()));

			// calculate extra charge
			rental = calculateExtraCharge(rental);

			// update rental
			rentalDAO.save(rental);

			rentals.add(rental);
		}

		// calculate total extra charge
		double extraTotalCharge = rentals.stream().mapToDouble(Rental::getExtraCharge).sum();

		ReturnResponse returnResponse = new ReturnResponse();
		returnResponse.setExtraCharge(new Float(extraTotalCharge));
		return returnResponse;
	}

}
