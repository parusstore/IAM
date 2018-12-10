package com.parus.store.controller;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parus.store.payload.AddAddressRequest;
import com.parus.store.payload.ApiResponse;
import com.parus.store.payload.DeleteAddressRequest;
import com.parus.store.payload.UpdateAddressRequest;
import com.parus.store.service.AddressService;
import com.parus.store.exception.ModifyAddressException;
import com.parus.store.model.Address;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@PostMapping("/address/add")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Add address in the current user profile")
	public ResponseEntity<?> addAdress(Authentication auth,@Valid @RequestBody AddAddressRequest addAddressRequest)
	{
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
		Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		Optional<Address> addressOptional = addressService.addAddress((String) details.get("user_name"),addAddressRequest);
		return ResponseEntity.ok(new ApiResponse("User Address has been updated successfully", true));
	}
	
	@PostMapping("/address/update")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Update address to the current user profile")
	public ResponseEntity<?> updateAdress(Authentication auth,@Valid @RequestBody UpdateAddressRequest updateAddressRequest)
	{
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
		Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		Optional<Address> addressOptional = addressService.updateAddress((String) details.get("user_name"),updateAddressRequest);
		addressOptional.orElseThrow(() -> new ModifyAddressException("Not able to find object" ,
				 updateAddressRequest.getAddressId()));
		return ResponseEntity.ok(new ApiResponse("Address has been added successfully", true));
	}
	
	@PostMapping("/address/delete")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Delete address in the current user profile")
	public ResponseEntity<?> deleteAddress(Authentication auth,@Valid @RequestBody DeleteAddressRequest deleteAddressRequest)
	{
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
		Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		Boolean result = addressService.deleteAddress((String) details.get("user_name"), deleteAddressRequest);
		if(!result)
		{
			throw new ModifyAddressException("Not able to find object" ,
					deleteAddressRequest.getAddressId());
		}
		return ResponseEntity.ok(new ApiResponse("Address has been deleted successfully", true));
	}
	
	
	
	@GetMapping("/address/findall")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Find address in the current user profile")
	public ResponseEntity<?> findAll(Authentication auth)
	{
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
		Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		Set<Address> addresses = addressService.getAddressForUser((String) details.get("user_name"));
		//Danger: I need to work on cverting set to json it is a must
		return ResponseEntity.ok(addresses);
	}
	
	//danger: add one more api to get adress based on the id of the adderssId man
	

}
