package ericarfs.socialmedia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.entity.util.Email;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(Email email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(Email email);
}
