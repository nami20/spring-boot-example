package net.gradle.com.springbootexpample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.gradle.com.springbootexpample.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
}