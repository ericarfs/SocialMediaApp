package ericarfs.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByIdAndSentToAndIsAnsweredFalse(Long id, User sentTo);

    Page<Question> findBySentToAndIsAnsweredFalse(User sentTo, Pageable pageable);

    boolean existsByIdAndSentTo(Long id, User sentTo);

    void deleteByIdAndSentTo(Long id, User sentTo);
}
