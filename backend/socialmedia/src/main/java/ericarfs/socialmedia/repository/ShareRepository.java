package ericarfs.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Share;
import ericarfs.socialmedia.entity.User;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Optional<Share> findByAnswerAndUser(Answer answer, User user);

    List<Share> findByAnswerId(Long answerId);
}
