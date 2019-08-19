package com.drago.spring.demo.repositories;

import com.drago.spring.demo.domain.Status;
import com.drago.spring.demo.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

	Page<User> findAllByStatus(Pageable pageable, Status status);



}
