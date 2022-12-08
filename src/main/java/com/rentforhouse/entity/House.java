package com.rentforhouse.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "house")
public class House extends BaseEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "area")
	private String area;
	
	@Column(name = "room_number")
	private Integer roomNumber;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "view")
	private Integer view;
	

	@Column(name = "status")
	private Boolean status;

	@Column(name = "price")
	private Float price;

	@Column(name = "image")
	private String image;
	
	@Column(name = "image2")
	private String image2;
	
	@Column(name = "image3")
	private String image3;
	
	@Column(name = "image4")
	private String image4;
	
	@Column(name = "image5")
	private String image5;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "house_type", joinColumns = @JoinColumn(name = "house_id", nullable = false),
				inverseJoinColumns = @JoinColumn(name = "type_id", nullable = false))
	private List<HouseType> houseTypes = new ArrayList<>();

	@OneToMany(mappedBy = "house", fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();

}
