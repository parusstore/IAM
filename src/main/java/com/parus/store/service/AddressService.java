package com.parus.store.service;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parus.store.config.jpa.repository.AddressRespository;
import com.parus.store.exception.ModifyAddressException;
import com.parus.store.exception.UserRegistrationException;
import com.parus.store.model.Address;
import com.parus.store.model.User;
import com.parus.store.payload.AddAddressRequest;
import com.parus.store.payload.DeleteAddressRequest;
import com.parus.store.payload.UpdateAddressRequest;

@Service
public class AddressService {
	
	@Autowired
	private AddressRespository addressRepository;
	
	@Autowired 
	private UserService userService;

	public Optional<Address> addAddress(String userName,AddAddressRequest addAddressRequest) {
		// TODO Auto-generated method stub
		 Optional<User> userOptional = userService.findByEmail(userName);
		 Address address = new Address();
		 address.setAddressline1(addAddressRequest.getAddressline1());
		 address.setAddressline2(addAddressRequest.getAddressline2());
		 address.setCity(addAddressRequest.getCity());
		 address.setZipCode(addAddressRequest.getZipCode());
		 address.setCountry(addAddressRequest.getCountry());
		 address.setUser(userOptional.get());
		 addressRepository.save(address);
		 return Optional.ofNullable(address);
	}
	
	
	public Optional<Address> updateAddress(String userName,UpdateAddressRequest updateAddressRequest) {
		// TODO Auto-generated method stub
		 Optional<User> userOptional = userService.findByEmail(userName);
		 Optional<Address> addressOptional = addressRepository.findByAddressId(Long.parseLong(updateAddressRequest.getAddressId()));
		 addressOptional.orElseThrow(() -> new ModifyAddressException("Not able to find object" ,
				 updateAddressRequest.getAddressId()));
		 //if(userOptional.get().getAddresses().contains(addressOptional.get()))
		 if(userOptional.get().getAddresses().stream().anyMatch(x->x.getAddressId() == Long.parseLong(updateAddressRequest.getAddressId())))
		 {
		 addressOptional.get().setAddressline1(updateAddressRequest.getAddressline1());
		 addressOptional.get().setAddressline2(updateAddressRequest.getAddressline2());
		 addressOptional.get().setCity(updateAddressRequest.getCity());
		 addressOptional.get().setZipCode(updateAddressRequest.getZipCode());
		 addressOptional.get().setCountry(updateAddressRequest.getCountry());
		 addressRepository.save(addressOptional.get());
		 return Optional.ofNullable(addressOptional.get());
		 }
		 return Optional.ofNullable(null);
		 
	}
	
	public Boolean deleteAddress(String userName,DeleteAddressRequest deleteAddressRequest)
	{
		//Danger: Check if user has access to the address
		 Optional<User> userOptional = userService.findByEmail(userName);
		 Optional<Address> addressOptional = addressRepository.findById(Long.parseLong(deleteAddressRequest.getAddressId()));
		 addressOptional.orElseThrow(() -> new ModifyAddressException("Not able to find object" ,
				 deleteAddressRequest.getAddressId()));
		 addressRepository.delete(addressOptional.get());
		 return true;
	}
	
	public Set<Address> getAddressForUser(String userName)
	{
		Optional<User> userOptional = userService.findByEmail(userName);
		return userOptional.get().getAddresses();		
	}
	

}
