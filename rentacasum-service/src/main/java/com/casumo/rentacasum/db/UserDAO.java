/**
 * 
 */
package com.casumo.rentacasum.db;

import org.hibernate.SessionFactory;

import com.casumo.rentacasum.core.User;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * User entity related DAO.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class UserDAO extends AbstractDAO<User> {

	public UserDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
