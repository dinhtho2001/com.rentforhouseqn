package com.rentforhouse.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineChartResponse {

	private List<String> NameColumns;
	private int TotalColumn;
	private List<?> dataHouses;
	private List<?> dataUsers;
	private Object totalDataHouses;
	private Object totalDataUsers;
}
