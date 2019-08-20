package com.drago.spring.demo.enums;

public enum RoleEnum {
	
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private String name;
	
	private RoleEnum(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
}
