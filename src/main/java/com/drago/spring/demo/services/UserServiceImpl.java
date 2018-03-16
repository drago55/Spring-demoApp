package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Role;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.domain.UserLoginDto;
import com.drago.spring.demo.domain.UserRegistrationDto;
import com.drago.spring.demo.exception.EmailExistsException;
import com.drago.spring.demo.repositories.RoleRepository;
import com.drago.spring.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public User findUserByEmail(String email) {

        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return userOptional.get();
    }

    @Override
    public User save(UserRegistrationDto userRegistrationDto) throws EmailExistsException {

        if (emailExist(userRegistrationDto.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + userRegistrationDto.getEmail());
        }
        User user = new User();
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        return userRepository.save(user);
    }

    @Override
    public boolean isValidUser(UserLoginDto userLoginDto) {

        if (passwordEncoder.matches(userLoginDto.getUserPassword(), findUserByEmail(userLoginDto.getUserEmail()).getPassword())) {
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByEmail(email);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    private boolean emailExist(String email) {
        User user = findUserByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}
