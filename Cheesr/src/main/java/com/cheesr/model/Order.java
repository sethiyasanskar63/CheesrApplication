package com.cheesr.model;

import java.io.Serializable;
import java.sql.Date;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer addressid;
	private Double orderAmount;
	private Integer userId;

	private Date orderDate;

	public Order() {
	}

	public Order(Integer id, Integer addressid, Double orderAmount, Integer userId, Date orderDate) {
		super();
		this.id = id;
		this.addressid = addressid;
		this.orderAmount = orderAmount;
		this.userId = userId;
		this.orderDate = orderDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAddressid() {
		return addressid;
	}

	public void setAddressid(Integer addressid) {
		this.addressid = addressid;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", addressid=" + addressid + ", orderAmount=" + orderAmount + ", userId=" + userId
				+ ", orderDate=" + orderDate + "]";
	}

}
