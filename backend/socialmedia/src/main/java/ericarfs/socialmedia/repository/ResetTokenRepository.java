package ericarfs.socialmedia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.ResetToken;
import ericarfs.socialmedia.entity.User;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByToken(String token);

    Optional<ResetToken> findByUser(User user);
}
