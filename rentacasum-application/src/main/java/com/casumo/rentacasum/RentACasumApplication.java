package com.casumo.rentacasum;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.casumo.rentacasum.auth.ExampleAuthenticator;
import com.casumo.rentacasum.auth.ExampleAuthorizer;
import com.casumo.rentacasum.core.Customer;
import com.casumo.rentacasum.core.Movie;
import com.casumo.rentacasum.core.Rental;
import com.casumo.rentacasum.core.User;
import com.casumo.rentacasum.db.CustomerDAO;
import com.casumo.rentacasum.db.MovieDAO;
import com.casumo.rentacasum.db.RentalDAO;
import com.casumo.rentacasum.resources.CustomersResource;
import com.casumo.rentacasum.resources.MoviesResource;
import com.casumo.rentacasum.resources.ProtectedResource;
import com.casumo.rentacasum.resources.RentalsResource;
import com.casumo.rentacasum.service.IRentalService;
import com.casumo.rentacasum.service.RentalServiceImpl;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RentACasumApplication extends Application<RentACasumConfiguration> {
	public static void main(String[] args) throws Exception {
		new RentACasumApplication().run(args);
	}

	private final HibernateBundle<RentACasumConfiguration> hibernateBundle = new HibernateBundle<RentACasumConfiguration>(
			Movie.class, Rental.class, Customer.class, com.casumo.rentacasum.core.User.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(RentACasumConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public String getName() {
		return "Rent-A-Casum";
	}

	@Override
	public void initialize(Bootstrap<RentACasumConfiguration> bootstrap) {
		// Enable variable substitution with environment variables
		bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
				bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

		bootstrap.addBundle(new MigrationsBundle<RentACasumConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(RentACasumConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
		bootstrap.addBundle(hibernateBundle);
	}

	@Override
	public void run(RentACasumConfiguration configuration, Environment environment) {
		final CustomerDAO customerDAO = new CustomerDAO(hibernateBundle.getSessionFactory());
		//unused for the moment, added for future use
//		final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());   
		final MovieDAO movieDAO = new MovieDAO(hibernateBundle.getSessionFactory());
		final RentalDAO rentalDAO = new RentalDAO(hibernateBundle.getSessionFactory());

		final IRentalService rentalService = new RentalServiceImpl(movieDAO, rentalDAO, customerDAO,
				configuration.getPremiumPrice(), configuration.getBasicPrice());

		// TODO: add health check
		environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		environment.jersey()
				.register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
						.setAuthenticator(new ExampleAuthenticator()).setAuthorizer(new ExampleAuthorizer())
						.setRealm("SUPER SECRET STUFF").buildAuthFilter()));
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		environment.jersey().register(new ProtectedResource());
		environment.jersey().register(new MoviesResource(movieDAO));
		environment.jersey().register(new RentalsResource(rentalDAO, customerDAO, rentalService));
		environment.jersey().register(new CustomersResource(customerDAO));
	}
}
