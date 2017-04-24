/**
 * 
 */
package com.casumo.rentacasum.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import com.casumo.rentacasum.core.Rental;
import java.util.Set;
import javax.persistence.OneToMany;

/**
 * Represents a store movie.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
@Entity
@Table(name = "movie")
@NamedQueries(
	    {
	        @NamedQuery(
	            name = "findAllMovies",
	            query = "SELECT m FROM Movie m"
	        )
	    }
	)
public class Movie extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8183770896966882682L;

	public enum Type {
		NEW_RELEASE, REGULAR, OLD
	}

	private String title;

	private String description;

	private Date releaseDate;

	private String imdbCode;

	@Enumerated(EnumType.STRING)
	private Type type;

	@OneToMany(mappedBy = "movie")
	private Set<Rental> rentals;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getImdbCode() {
		return imdbCode;
	}

	public void setImdbCode(String imdbCode) {
		this.imdbCode = imdbCode;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Set<Rental> getRentals() {
	    return rentals;
	}

	public void setRentals(Set<Rental> param) {
	    this.rentals = param;
	}
	
}
