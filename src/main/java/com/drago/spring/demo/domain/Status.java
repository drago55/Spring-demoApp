package com.drago.spring.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer statusCode;

	private String description;

	public Status(Integer statusCode, String description) {
		this.statusCode = statusCode;
		this.description = description;
	}

	public Status() {
	}

}
