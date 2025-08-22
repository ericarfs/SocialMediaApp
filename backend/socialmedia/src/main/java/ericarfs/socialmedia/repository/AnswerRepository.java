package ericarfs.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.User;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndAuthor(Long id, User author);

    List<Answer> findByAuthor(User author);

    @Query(value = "SELECT a.id, a.question_id, a.author_id, a.body, a.updated_at, CASE WHEN a.author_id=:userId THEN a.created_at ELSE s.share_date END AS created_at FROM answers AS a LEFT JOIN answers_shares AS s ON a.id= s.answer_id AND s.user_id=:userId WHERE a.author_id=:userId OR s.user_id=:userId ORDER BY created_at DESC", nativeQuery = true)
    List<Answer> findAuthoredAndSharedAnswers(Long userId);

    boolean existsByIdAndAuthor(Long id, User author);
}
