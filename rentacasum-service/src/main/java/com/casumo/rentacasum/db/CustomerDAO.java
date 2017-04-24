/**
 * 
 */
package com.casumo.rentacasum.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.casumo.rentacasum.core.Customer;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * Customer entity related DAO.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class CustomerDAO extends AbstractDAO<Customer> {

	public CustomerDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<Customer> findById(Long id) {
		return Optional.ofNullable(get(id));
	}

	/**
	 * Creates or updates a customer.
	 * 
	 * @param customer
	 *            The {@link Customer} to persist
	 * @return The new or updated instance.
	 */
	public Customer save(Customer customer) {
		return persist(customer);
	}

	/**
	 * Searches customers using a textual term ('contains' style like search) on
	 * various {@link Customer} fields
	 * 
	 * @param term
	 *            The term to search for.
	 * @return a row count of customers.
	 */
	public Long countByTerm(String term) {
		Query<Long> query = currentSession().createQuery(
				"select count(*) from Customer c where c.email like :term or c.firstname like :term or c.lastname  like :term",
				Long.class);
		query.setParameter("term", "%" + term + "%");
		return query.uniqueResult();
	}
	
	/**
	 * Searches customers  using a textual term ('contains' style like search) on
	 * various {@link Customer} fields.
	 * 
	 * @param term
	 *            The term to search for.
	 * @param startPosition
	 *            starting row.
	 * @param maxResult
	 *            maximum number of rows to fetch.
	 * @return a list of customers, or an empty list if no resuts found.
	 */
	public List<Customer> findByTerm(String term, int startPosition, int maxResult) {
		Query<Customer> query = query(
				"from Customer c where c.email like :term or c.firstname like :term or c.lastname  like :term");
		query.setParameter("term", "%" + term + "%");
		query.setFirstResult(startPosition);
		query.setMaxResults(maxResult);
		return list(query);
	}

}
