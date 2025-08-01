package com.example.stage5.repository;

import com.example.stage5.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :id")
    List<Role> findRolesByUserId(Long id);


    Optional<Role> findByRoleName(String role);
}
