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

	@Column(name = "detailsumary", columnDefinition = "TEXT")
	private String detailSumary;

	@Column(name = "price")
	private Float price;

	@Column(name = "image")
	private String image;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "house_type", joinColumns = @JoinColumn(name = "house_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "type_id", nullable = false))
	private List<HouseType> houseTypes = new ArrayList<>();

	@OneToMany(mappedBy = "house", fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetailSumary() {
		return detailSumary;
	}

	public void setDetailSumary(String detailSumary) {
		this.detailSumary = detailSumary;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<HouseType> getHouseTypes() {
		return houseTypes;
	}

	public void setHouseTypes(List<HouseType> houseTypes) {
		this.houseTypes = houseTypes;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
