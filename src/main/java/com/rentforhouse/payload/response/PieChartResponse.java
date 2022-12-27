package com.rentforhouse.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PieChartResponse {

	private List<String> NameColumns;
	private int TotalColumn;
	private List<?> datas;
}
