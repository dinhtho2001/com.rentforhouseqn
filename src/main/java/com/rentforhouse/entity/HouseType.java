package com.rentforhouse.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "type")
public class HouseType extends BaseEntity {

	@Column(name = "name")
	private String name;
	
	@Column(name= "code")
	private String code;

	@ManyToMany(mappedBy = "houseTypes", fetch = FetchType.LAZY)
	private List<House> houses = new ArrayList<>();
	
	public void remove(House b) {
		houses.remove(b);
		b.getHouseTypes().remove(this);
	}  

}
