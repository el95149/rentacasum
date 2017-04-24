/**
 * 
 */
package com.casumo.rentacasum.api;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response provided when returning movies.
 * 
 * @author <a href=
 *         "mailto:angelosanagnostopoulos@runbox.com">aanagnostopoulos</a>
 *
 */
public class ReturnResponse {

	private Float extraCharge;

	@JsonProperty
	public Float getExtraCharge() {
		return extraCharge;
	}

	public void setExtraCharge(Float charge) {
		this.extraCharge = charge;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
	}
}
