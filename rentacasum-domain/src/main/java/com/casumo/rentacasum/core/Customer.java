package com.casumo.rentacasum.core;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import com.casumo.rentacasum.core.Rental;

import java.util.Date;
import java.util.Set;
import javax.persistence.OneToMany;

/**
 * Represents a movie store customer
 * 
 */
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6911376366125012332L;

	public enum Gender {
		MALE, FEMALE;
	}

	private Date birthday;

	private String comments;

	private String email;

	private String firstname;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String lastname;

	private String organization;

	private String password;

	private String promotionCode;

	private String username;
	
	private Integer totalBonusPoints;

	@OneToMany(mappedBy = "customer")
	private Set<Rental> rentals;


	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}


	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Integer getTotalBonusPoints() {
		return totalBonusPoints;
	}

	public void setTotalBonusPoints(Integer totalBonusPoints) {
		this.totalBonusPoints = totalBonusPoints;
	}

	public Set<Rental> getRentals() {
	    return rentals;
	}

	public void setRentals(Set<Rental> param) {
	    this.rentals = param;
	}

}