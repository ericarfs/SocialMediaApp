package ericarfs.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByIdAndSentTo(Long id, User sentTo);

    List<Question> findBySentTo(User sentTo);

    boolean existsByIdAndSentTo(Long id, User sentTo);

    void deleteByIdAndSentTo(Long id, User sentTo);
}
