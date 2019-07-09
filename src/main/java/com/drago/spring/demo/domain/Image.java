package com.drago.spring.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;

	private String imagePath;

	/**
	 * Image
	 *
	 * @param fileName  original name of image
	 * @param imagePath path of image
	 */
	public Image(String fileName, String imagePath) {

		this.fileName = fileName;
		this.imagePath = imagePath;
	}

	public Image() {
	}
}
