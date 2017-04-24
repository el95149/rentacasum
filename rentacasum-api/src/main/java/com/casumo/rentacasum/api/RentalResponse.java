/**
 * 
 */
package com.casumo.rentacasum.api;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response provided when renting movies.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class RentalResponse {

	private Float charge;

	private Integer bonusPoints;

	private Integer totalBonusPoints;

	@JsonProperty
	public Float getCharge() {
		return charge;
	}

	public void setCharge(Float charge) {
		this.charge = charge;
	}

	@JsonProperty
	public Integer getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(Integer bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	@JsonProperty
	public Integer getTotalBonusPoints() {
		return totalBonusPoints;
	}

	public void setTotalBonusPoints(Integer totalBonusPoints) {
		this.totalBonusPoints = totalBonusPoints;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}
}
