package com.casumo.rentacasum;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.casumo.rentacasum.api.BatchRentalRequest;
import com.casumo.rentacasum.api.MovieRentalRequest;

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class IntegrationTest {

	private static final String TMP_FILE = createTempFile();
	private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-example.yml");

	@ClassRule
	public static final DropwizardAppRule<RentACasumConfiguration> RULE = new DropwizardAppRule<>(
			RentACasumApplication.class, CONFIG_PATH, ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

	@BeforeClass
	public static void migrateDb() throws Exception {
		RULE.getApplication().run("db", "migrate", CONFIG_PATH);
	}

	private static String createTempFile() {
		try {
			return File.createTempFile("test-example", null).getAbsolutePath();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Test
	public void testRentMovies() {
		BatchRentalRequest rentalRequest = new BatchRentalRequest();
		rentalRequest.setCustomerId(1L);
		MovieRentalRequest movieRentalRequest = new MovieRentalRequest();
		movieRentalRequest.setDays(5);
		movieRentalRequest.setMovie(1L);
		MovieRentalRequest[] movies = { movieRentalRequest };
		rentalRequest.setMovies(movies);

		final Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/rentals").request()
				.post(Entity.entity(rentalRequest, MediaType.APPLICATION_JSON_TYPE));
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		
	}

}
