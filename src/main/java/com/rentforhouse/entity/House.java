package com.rentforhouse.entity;

import java.util.ArrayList;
import java.util.List;

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

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "price")
	private Float price;

	@Column(name = "image")
	private String image;

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
