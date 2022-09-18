package com.rentforhouse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity{

	@Column(name = "content",columnDefinition = "TEXT")
    private String content;
	
	@Column(name = "user_id")
	private Long userId;
	
	@ManyToOne
	@JoinColumn(name = "house_id")
	private HouseEntity house;

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

	public HouseEntity getHouse() {
		return house;
	}

	public void setHouse(HouseEntity house) {
		this.house = house;
	}


	
	
	
	
}
