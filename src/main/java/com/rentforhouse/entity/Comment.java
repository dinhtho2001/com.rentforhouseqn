package com.rentforhouse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "comment")
public class Comment extends BaseEntity {

	@Column(name = "content" , columnDefinition = "TEXT")
	private String content;

	@Column(name = "user_id")
	private Long userId;
	
	@ManyToOne
	@JoinColumn(name = "house_id")
	private House house;
}
