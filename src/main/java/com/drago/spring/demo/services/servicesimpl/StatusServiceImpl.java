package com.drago.spring.demo.services.servicesimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drago.spring.demo.domain.Status;
import com.drago.spring.demo.enums.StatusEnum;
import com.drago.spring.demo.repositories.StatusRepository;
import com.drago.spring.demo.services.StatusService;

@Service
public class StatusServiceImpl implements StatusService {

	@Autowired
	private StatusRepository statusRepository;

	@Override
	public Status getStatusByCode(StatusEnum statusEnum) {
		
		List<Status> listOfStatus = statusRepository.findAll();
		Optional<Status> optionaOfStatus = listOfStatus.stream().filter(status -> status.getStatusCode().equals(statusEnum.getStatusCode())).findFirst();
		if (!optionaOfStatus.isPresent()) {
			throw new IllegalStateException("Status doesn't exist in database!");
		}
		return optionaOfStatus.get();
	}

}
