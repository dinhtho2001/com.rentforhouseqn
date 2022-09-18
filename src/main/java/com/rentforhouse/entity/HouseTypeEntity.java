package com.rentforhouse.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "type")
public class HouseTypeEntity extends BaseEntity{

	@Column(name = "name", nullable = false)
    private String name;
	
	 @ManyToMany(mappedBy = "houseTypes", fetch = FetchType.LAZY)
	 private List<HouseEntity> houses = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HouseEntity> getHouses() {
		return houses;
	}

	public void setHouses(List<HouseEntity> houses) {
		this.houses = houses;
	}
	 
	 
	
}
