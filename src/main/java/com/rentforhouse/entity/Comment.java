package com.rentforhouse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

	@Column(name = "content"/* , columnDefinition = "TEXT" */)
	private String content;

	@Column(name = "user_id")
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "house_id")
	private House house;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

}
