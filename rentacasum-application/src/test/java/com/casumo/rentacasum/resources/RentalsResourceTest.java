/**
 * 
 */
package com.casumo.rentacasum.resources;

import com.casumo.rentacasum.api.BatchRentalRequest;
import com.casumo.rentacasum.api.MovieRentalRequest;
import com.casumo.rentacasum.api.RentalResponse;
import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.core.Movie;
import com.casumo.rentacasum.core.Rental;
import com.casumo.rentacasum.db.CustomerDAO;
import com.casumo.rentacasum.db.RentalDAO;
import com.casumo.rentacasum.service.IRentalService;
import com.casumo.rentacasum.service.RentalServiceImpl;

import io.dropwizard.testing.junit.ResourceTestRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link RentalsResource}
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class RentalsResourceTest {

	private static final RentalDAO RENTAL_DAO = mock(RentalDAO.class);

	private static final CustomerDAO CUSTOMER_DAO = mock(CustomerDAO.class);

	private static final IRentalService RENTAL_SERVICE = mock(RentalServiceImpl.class);

	@ClassRule
	public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
			.addResource(new RentalsResource(RENTAL_DAO, CUSTOMER_DAO, RENTAL_SERVICE)).build();

	private Rental rental;
	private BatchRentalRequest batchRentalRequest;
	private RentalResponse rentalResponse;

	@Before
	public void setUp() {
		rental = new Rental();
		Date now = new Date();
		rental.setRentalDate(now);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, 2);
		rental.setDeclaredReturnDate(calendar.getTime());
		rental.setActualReturnDate(calendar.getTime());
		rental.setInitialCharge(40f);
		Customer customer = new Customer();
		customer.setId(1L);
		rental.setCustomer(customer);
		Movie movie = new Movie();
		movie.setId(1L);
		rental.setMovie(movie);

		batchRentalRequest = new BatchRentalRequest();
		batchRentalRequest.setCustomerId(1L);
		MovieRentalRequest movieRentalRequest = new MovieRentalRequest();
		movieRentalRequest.setDays(5);
		movieRentalRequest.setMovie(1L);
		MovieRentalRequest[] movies = { movieRentalRequest };
		batchRentalRequest.setMovies(movies);

		rentalResponse = new RentalResponse();
		rentalResponse.setCharge(40f);

		when(RENTAL_SERVICE.rentMovies(any(BatchRentalRequest.class))).thenReturn(rentalResponse);
	}

	@After
	public void tearDown() {
		reset(RENTAL_DAO, CUSTOMER_DAO, RENTAL_SERVICE);
	}

	@Test
	public void rentMovies() {
		final Response response = RESOURCES.target("/rentals").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(batchRentalRequest, MediaType.APPLICATION_JSON_TYPE));
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
	}
}
