/**
 * 
 */
package com.casumo.rentacasum.api;

import java.util.List;

import com.casumo.rentacasum.core.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains {@link Customer} instances, providing a JSON-friendly
 * representation.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class CustomersRepresentation {

	private final Long totalRows;

	private final Integer start;

	private final Integer rows;

	private final List<CustomerRepresentation> customers;

	public CustomersRepresentation(Long totalRows, Integer start, Integer rows,
			List<CustomerRepresentation> customers) {
		super();
		this.totalRows = totalRows;
		this.start = start;
		this.rows = rows;
		this.customers = customers;
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
	public List<CustomerRepresentation> getCustomers() {
		return customers;
	}

}
