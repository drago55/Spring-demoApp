package com.drago.spring.demo.services.servicesimpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drago.spring.demo.data_transfer_objects.UserProfileDto;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.domain.UserProfile;
import com.drago.spring.demo.repositories.UserProfileRepository;
import com.drago.spring.demo.services.UserProfileService;
import com.drago.spring.demo.services.UserService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserProfileDto getUserProfile() {
		User user = userService.getAuthenticatedUser();

		Optional<UserProfile> userProfile = userProfileRepository.findByUserId(user.getId());

		return modelMapper.map(userProfile.orElse(new UserProfile()), UserProfileDto.class);
	}

	@Override
	public UserProfileDto save(UserProfileDto userProfileDto) {
		
		UserProfile newUserProfile = modelMapper.map(userProfileDto, UserProfile.class);
		User user = userService.getAuthenticatedUser();
		newUserProfile.setUser(user);
		return modelMapper.map(userProfileRepository.save(newUserProfile), UserProfileDto.class);
	}

}
