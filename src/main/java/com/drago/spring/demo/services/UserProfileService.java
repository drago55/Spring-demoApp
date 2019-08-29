package com.drago.spring.demo.services;

import com.drago.spring.demo.data_transfer_objects.UserProfileDto;

public interface UserProfileService {
	
	UserProfileDto getUserProfile();

	UserProfileDto save(UserProfileDto userProfileDto);

}
