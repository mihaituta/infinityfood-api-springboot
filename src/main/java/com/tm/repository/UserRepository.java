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

    //@Query(value = "SELECT * FROM users WHERE role_id = :roleId", nativeQuery = true)
    //List<UserResponseDTO> findUserByRoleId(@Param("roleId") String roleId);

    //@Query("SELECT new com.tm.dto.UserResponseDTO(u.id, u.name) FROM User u WHERE u.roleId = :roleId")
    //List<UserResponseDTO> findUserByRoleId(@Param("roleId") Role roleId);

    //@Query("SELECT new com.tm.dto.UserResponseDTO(u.id, u.name) FROM User u WHERE u.roleId = :roleId")
    //List<UserIdNameProjection> findUserByRoleId(@Param("roleId") Role roleId);

    List<UserIdNameProjection> findUserByRoleId(Role roleId);
}
