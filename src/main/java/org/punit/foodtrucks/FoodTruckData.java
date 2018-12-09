package org.punit.foodtrucks;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodTruckData {
	
	@JsonProperty("applicant")
	private String name;
	
	@JsonProperty("location")
	private String address;
	
	@JsonProperty("start24")
	private String start24;
	
	@JsonProperty("end24")
	private String end24;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStart24() {
		return start24;
	}
	public void setStart24(String start24) {
		this.start24 = start24;
	}
	public String getEnd24() {
		return end24;
	}
	public void setEnd24(String end24) {
		this.end24 = end24;
	}
}
