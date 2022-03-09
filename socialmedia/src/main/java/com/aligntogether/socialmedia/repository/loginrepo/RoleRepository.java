package com.aligntogether.socialmedia.repository.loginrepo;

import com.aligntogether.socialmedia.model.login.Role;
import com.aligntogether.socialmedia.model.login.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
