package com.padma.parus.store.mail.service.payload;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class AccountChange implements MailPayload{
	
	
	@NotBlank(message = "action can not be null")
	@ApiModelProperty(value = "valid action is a must", required = true, allowableValues = "NonEmpty String")
	private String action;


	@NotBlank(message = "actionStatus can not be null")
	@ApiModelProperty(value = "Valid to actionStatus is a must", required = true, allowableValues = "NonEmpty String")
	private String actionStatus;
	
	@NotBlank(message = "to address can not be null")
	@ApiModelProperty(value = "Valid to address is a must", required = true, allowableValues = "NonEmpty String")
	private String to;
	
	public AccountChange()
	{
		
	}
	
	public AccountChange(String action,String actionStatus,String to)
	{
		this.action=action;
		this.actionStatus=actionStatus;
		this.to=to;
	}
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}


	









}
