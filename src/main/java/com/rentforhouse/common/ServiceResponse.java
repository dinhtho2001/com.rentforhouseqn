package com.rentforhouse.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceResponse {

	private Boolean state;
	private Object data;
	private String message;
	private ServerError serverError;
	
	
	public ServiceResponse(Boolean state, Object data) {
        this.state = state;
        this.data = data;
        this.serverError = new ServerError();
        setState(state);
    }
	public ServiceResponse(ServerError serverError) {
		this.state = false;
        this.serverError = serverError;
        setState(state);
    }
	
	// Setter cho thuộc tính state để tự động thiết lập giá trị cho message
    public void setState(Boolean state) {
        this.state = state;
        if (state) {
            this.message = "successfully";
        } else {
            this.message = "failure";
        }
    }
}
