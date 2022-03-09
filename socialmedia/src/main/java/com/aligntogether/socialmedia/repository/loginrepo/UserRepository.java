package com.aligntogether.socialmedia.repository.loginrepo;

import com.aligntogether.socialmedia.model.login.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM Users u",nativeQuery = true)
    long countAllUsers();

    @Query(value = "SELECT u.posts FROM users u WHERE id = ?1", nativeQuery = true)
    String findByPosts(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
