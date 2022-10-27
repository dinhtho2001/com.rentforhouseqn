package com.rentforhouse.payload.response;

import java.util.List;

import com.rentforhouse.dto.HouseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseResponse {
	private int page;
	private int total_page;
	private Object houses;

	
}
