package com.parus.store.config.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parus.store.model.Address;
import com.parus.store.model.Role;
import java.lang.Long;
import java.util.List;
import java.util.Optional;

public interface AddressRespository extends JpaRepository<Address, Long>{
	
	Optional<Address> findByAddressId(Long addressId);

}
