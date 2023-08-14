package com.cheesr.model;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String street;
	private Integer zipcode;
	private String city;
	private Integer userId;
	private String state;

	public Address() {
	}

	public Address(Integer id, String name, String street, Integer zipcode, String city, Integer userId, String state) {
		super();
		this.id = id;
		this.name = name;
		this.street = street;
		this.zipcode = zipcode;
		this.city = city;
		this.userId = userId;
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getZipcode() {
		return zipcode;
	}

	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return (name + ", " + state + ", " + city + ", " + state + ", " + zipcode);
	}

}
