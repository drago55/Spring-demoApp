package com.drago.spring.demo.repositories;

import com.drago.spring.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
