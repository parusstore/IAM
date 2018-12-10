package com.parus.store.payload;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Delete Address payload", description = "Payload to delete address of user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAddressRequest {

	@NotBlank(message = "Address Id cannot be blank")
	@ApiModelProperty(value = "Address Id", required = true, allowableValues = "NonEmpty String")
	private String addressId;
}
