package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Status;
import com.drago.spring.demo.enums.StatusEnum;

public interface StatusService {
	
	Status getStatusByCode(StatusEnum statusEnum);

}
