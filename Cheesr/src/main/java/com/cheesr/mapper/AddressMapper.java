package com.cheesr.mapper;

import java.util.List;

import com.cheesr.model.Address;

public interface AddressMapper {

	void insertAddress(Address address);

	Address getAddressById(Integer id);

	List<Address> getAllAddress();

	List<Address> getAllAddressByUserId(Integer id);

}
