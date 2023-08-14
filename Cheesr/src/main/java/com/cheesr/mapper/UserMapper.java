package com.cheesr.mapper;

import com.cheesr.model.User;

public interface UserMapper {

	User getUserByUsername(String username);

	void insertUser(User user);

}
