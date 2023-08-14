package com.cheesr.mapper;

import java.util.List;

import com.cheesr.model.Order;

public interface OrderMapper {

	void insertOrder(Order order);

	List<Order> getAllOrdersByUserId(Integer id);
}
