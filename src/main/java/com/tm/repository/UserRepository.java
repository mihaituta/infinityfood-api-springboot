package com.tm.repository;

import com.tm.model.User;
import com.tm.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    boolean existsUserByName(String name);
    List<UserIdNameProjection> findUserByRoleId(Role roleId);
}
