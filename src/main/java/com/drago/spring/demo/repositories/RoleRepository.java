package com.drago.spring.demo.repositories;

import com.drago.spring.demo.domain.Role;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Set<Role> findAllByName(String name);
}
