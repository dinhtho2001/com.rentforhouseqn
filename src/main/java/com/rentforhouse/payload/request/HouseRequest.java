package com.rentforhouse.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseRequest {

	private String name ;
	private Long typeId;
	private Integer page;
	private Integer limit;

	
}
