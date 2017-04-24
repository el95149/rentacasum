/**
 * 
 */
package com.casumo.rentacasum.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casumo.rentacasum.api.BatchRentalRequest;
import com.casumo.rentacasum.api.BatchReturnRequest;
import com.casumo.rentacasum.api.RentalRepresentation;
import com.casumo.rentacasum.api.RentalResponse;
import com.casumo.rentacasum.api.ReturnResponse;
import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.core.Rental;
import com.casumo.rentacasum.db.CustomerDAO;
import com.casumo.rentacasum.db.RentalDAO;
import com.casumo.rentacasum.exception.CustomerNotFoundException;
import com.casumo.rentacasum.exception.DataCorruptionException;
import com.casumo.rentacasum.exception.MovieAlreadyRentedException;
import com.casumo.rentacasum.exception.MovieNotFoundException;
import com.casumo.rentacasum.exception.RentACasumException;
import com.casumo.rentacasum.exception.RentalNotFoundException;
import com.casumo.rentacasum.service.IRentalService;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

/**
 * * Resource related to batch operations on the {@link Rental} entity.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
@Path("/rentals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RentalsResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalsResource.class);

	private final RentalDAO rentalDAO;

	private final CustomerDAO customerDAO;

	private final IRentalService rentalService;

	public RentalsResource(RentalDAO rentalDAO, CustomerDAO customerDAO, IRentalService rentalService) {
		super();
		this.rentalDAO = rentalDAO;
		this.customerDAO = customerDAO;
		this.rentalService = rentalService;
	}

	@GET
	@UnitOfWork
	public List<RentalRepresentation> findActiveByCustomer(
			@UnwrapValidatedValue @QueryParam("customerId") @NotNull LongParam customerId) {

		Customer customer = customerDAO.findById(customerId.get())
				.orElseThrow(() -> new NotFoundException("No such customer."));

		List<Rental> activeRentals = rentalDAO.findActiveRentals(customer);
		if (activeRentals.isEmpty()) {
			throw new NotFoundException();
		}
		return activeRentals.stream().map(RentalRepresentation::new).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Rent a batch of movies.
	 * 
	 * @param batchRentalRequest
	 *            The {@link BatchRentalRequest} instance, containing a list of
	 *            movies the customer wants to rent.
	 * @return a {@link RentalResponse} instance, with the result of the rental
	 *         operation, if successfull.
	 */
	@POST
	@UnitOfWork
	public RentalResponse rentMovies(@NotNull @Valid BatchRentalRequest batchRentalRequest) {
		try {
			return rentalService.rentMovies(batchRentalRequest);
		} catch (DataCorruptionException e) {
			throw new InternalServerErrorException(e.getMessage(), e);
		} catch (MovieAlreadyRentedException e) {
			throw new WebApplicationException(e.getMessage(), e, Status.CONFLICT);
		} catch (RentACasumException e) {
			throw new BadRequestException(e.getMessage(), e);
		}
	}

	@POST
	@Path("/return")
	@UnitOfWork
	public ReturnResponse returnMovies(@NotNull @Valid BatchReturnRequest batchReturnRequest) {
		try {
			return rentalService.returnMovies(batchReturnRequest);
		} catch (DataCorruptionException e) {
			throw new InternalServerErrorException(e.getMessage(), e);
		} catch (RentACasumException e) {
			throw new BadRequestException(e.getMessage(), e);
		}
	}

}
