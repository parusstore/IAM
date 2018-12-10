package com.parus.store.payload;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Address payload", description = "Payload to add or update address of user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddAddressRequest {

	@NotBlank(message = "addressline1 cannot be blank")
	@ApiModelProperty(value = "User addressline1", required = true, allowableValues = "NonEmpty String")
	private String addressline1;
	
	@ApiModelProperty(value = "User addressline2", required = true, allowableValues = "NonEmpty String")
	private String addressline2;
	
	@NotBlank(message = "ZipCode cannot be blank")
	@ApiModelProperty(value = "User rZipCode", required = true, allowableValues = "NonEmpty String")
	private String zipCode;

	@NotBlank(message = "City cannot be blank")
	@ApiModelProperty(value = "User city", required = true, allowableValues = "NonEmpty String")
	private String city;
	
	@NotBlank(message = "Country cannot be blank")
	@ApiModelProperty(value = "Country of User", required = true, allowableValues = "NonEmpty String")
	private String country;
}
