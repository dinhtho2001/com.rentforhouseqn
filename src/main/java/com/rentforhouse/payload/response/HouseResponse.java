package com.rentforhouse.payload.response;

import java.util.List;

import com.rentforhouse.dto.HouseDto;

public class HouseResponse {
	private int page;
	private int total_page;
	private List<HouseDto> houses;

	public HouseResponse() {
	}

	public HouseResponse(int page, int total_page, List<HouseDto> houses) {
		this.page = page;
		this.total_page = total_page;
		this.houses = houses;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public List<HouseDto> getHouses() {
		return houses;
	}

	public void setHouses(List<HouseDto> houses) {
		this.houses = houses;
	}

}
