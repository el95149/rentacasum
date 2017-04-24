package com.casumo.rentacasum.db;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.core.Movie;
import com.casumo.rentacasum.core.Rental;

import io.dropwizard.testing.junit.DAOTestRule;

public class RentalDAOTest {

	@Rule
	public DAOTestRule daoTestRule = DAOTestRule.newBuilder().addEntityClass(Rental.class)
			.addEntityClass(Customer.class).addEntityClass(Movie.class).build();

	private RentalDAO rentalDAO;

	private CustomerDAO customerDAO;

	private MovieDAO movieDAO;

	@Before
	public void setUp() throws Exception {
		rentalDAO = new RentalDAO(daoTestRule.getSessionFactory());
		customerDAO = new CustomerDAO(daoTestRule.getSessionFactory());
		movieDAO = new MovieDAO(daoTestRule.getSessionFactory());
	}

	@Test
	public void createRental() {
		Date rentalDate = new Date();
		final Rental rental = daoTestRule.inTransaction(() -> {
			Customer testCustomer = new Customer();
			testCustomer = customerDAO.save(testCustomer);

			Movie testMovie = new Movie();
			testMovie = movieDAO.save(testMovie);

			Rental testRental = new Rental();
			testRental.setCustomer(testCustomer);
			testRental.setMovie(testMovie);
			testRental.setRentalDate(rentalDate);
			testRental.setInitialCharge(40f);
			return rentalDAO.save(testRental);
		});
		assertThat(rental.getId()).isGreaterThan(0);
		assertThat(rental.getCustomer()).isNotNull();
		assertThat(rental.getMovie()).isNotNull();
		assertThat(rental.getInitialCharge()).isEqualTo(40f);
		assertThat(rental.getRentalDate()).isEqualTo(rentalDate);
		assertThat(rentalDAO.findById(rental.getId())).isEqualTo(Optional.of(rental));
	}

	@Test
	public void findActiveRental() {
		Date rentalDate = new Date();
		final Customer testCustomer = new Customer();
		final Movie testMovie = new Movie();
		final Rental rental = daoTestRule.inTransaction(() -> {
			customerDAO.save(testCustomer);
			movieDAO.save(testMovie);

			Rental testRental = new Rental();
			testRental.setCustomer(testCustomer);
			testRental.setMovie(testMovie);
			testRental.setRentalDate(rentalDate);
			testRental.setInitialCharge(40f);
			return rentalDAO.save(testRental);
		});
		Optional<Rental> optionalRental = rentalDAO.findActiveRental(testMovie, testCustomer);
		assertThat(optionalRental.isPresent()).isEqualTo(true);
		assertThat(optionalRental.get().getId()).isEqualTo(rental.getId());
		assertThat(optionalRental.get().getMovie().getId()).isEqualTo(testMovie.getId());
		assertThat(optionalRental.get().getCustomer().getId()).isEqualTo(testCustomer.getId());
		assertThat(optionalRental.get().getActualReturnDate()).isNull();
	}

}
