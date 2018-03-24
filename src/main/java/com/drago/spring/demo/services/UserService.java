package com.drago.spring.demo.services;

        import com.drago.spring.demo.data_transfer_objects.UserLoginDto;
        import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
        import com.drago.spring.demo.domain.User;
        import com.drago.spring.demo.exception.InvalidUserException;
        import org.springframework.security.core.userdetails.UserDetailsService;

        import java.util.List;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);

    User save(UserRegistrationDto userRegistrationDto);

    boolean isValidUser(UserLoginDto userLoginDto) throws InvalidUserException;

    List<User> findAll();

    User findUseById(Long id);



}
