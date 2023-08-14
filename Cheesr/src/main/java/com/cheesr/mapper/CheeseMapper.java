package com.cheesr.mapper;

import java.util.List;

import com.cheesr.model.Cheese;

public interface CheeseMapper {
	Cheese getCheeseById(Integer id);

	List<Cheese> getAllCheese();
}
