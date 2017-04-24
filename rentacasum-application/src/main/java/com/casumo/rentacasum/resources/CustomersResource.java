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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.casumo.rentacasum.api.CustomerRepresentation;
import com.casumo.rentacasum.api.CustomersRepresentation;
import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.db.CustomerDAO;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;

/**
 * REST end-points related to customer operations.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomersResource {

	private final CustomerDAO customerDAO;

	public CustomersResource(CustomerDAO customerDAO) {
		super();
		this.customerDAO = customerDAO;
	}

	@GET
	@UnitOfWork
	@Path("/{customerId}")
	public CustomerRepresentation getCustomer(@PathParam("customerId") LongParam customerId) {
		return customerDAO.findById(customerId.get()).map(CustomerRepresentation::new)
				.orElseThrow(() -> new NotFoundException("No such customer."));
	}

	@GET
	@UnitOfWork
	public CustomersRepresentation findByTerm(@QueryParam("term") @NotNull String term,
			@QueryParam("start") @DefaultValue(QUERY_START_DEFAULT) IntParam start,
			@QueryParam("rows") @DefaultValue(QUERY_ROWS_DEFAULT) IntParam rows) {

		Long count = customerDAO.countByTerm(term);
		if (count == 0) {
			throw new NotFoundException("No customers found matching the criteria");
		}
		List<Customer> customers = customerDAO.findByTerm(term, start.get(), rows.get());

		List<CustomerRepresentation> customerList = customers.stream().map(CustomerRepresentation::new)
				.collect(Collectors.toCollection(ArrayList::new));

		return new CustomersRepresentation(count, start.get(), rows.get(), customerList);
	}
}
