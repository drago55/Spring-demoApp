package com.drago.spring.demo.enums;

public enum StatusEnum {

	ACTIVE(1), INACTIVE(0);

	private Integer status;

	private StatusEnum(Integer status) {
		this.status = status;
	}

	public Integer getStatusCode() {
		return status;
	}

}
