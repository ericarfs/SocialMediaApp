package ericarfs.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
  Optional<Answer> findByIdAndAuthor(Long id, User author);

  List<Answer> findByAuthor(User author);

  @Query(value = """
      SELECT
        a.id,
        a.question_id,
        a.author_id,
        a.body,
        a.updated_at,
        CASE WHEN a.author_id=:userId THEN a.created_at
        ELSE s.share_date END AS created_at
      FROM answers AS a
      LEFT JOIN answers_shares AS s ON a.id= s.answer_id AND s.user_id=:userId
      WHERE a.author_id=:userId OR s.user_id=:userId
      ORDER BY created_at DESC
      """, nativeQuery = true)
  Page<Answer> findAuthoredAndSharedAnswers(Long userId, Pageable pageable);

  @Query(value = """
      WITH following AS (
        SELECT following_id
        FROM users_following
        WHERE followers_id = :userId
      )
      SELECT
        a.id,
        a.question_id,
        a.author_id,
        a.body,
        a.updated_at,
        CASE
          WHEN a.author_id IN (SELECT following_id FROM following) THEN a.created_at
          ELSE s.share_date
        END AS created_at
      FROM
        answers AS a
        LEFT JOIN answers_shares AS s
          ON a.id = s.answer_id AND s.user_id IN (SELECT following_id FROM following)
      WHERE
        a.author_id IN (SELECT following_id FROM following)
        OR s.user_id IN (SELECT following_id FROM following)
      ORDER BY created_at DESC
      """, countQuery = """
      SELECT COUNT(*)
      FROM answers AS a
      WHERE a.author_id IN (
        SELECT following_id FROM users_following WHERE followers_id = :userId
      )
      """, nativeQuery = true)
  Page<Answer> findFollowersActivities(@Param("userId") Long userId, Pageable pageable);

  boolean existsByIdAndAuthor(Long id, User author);

  boolean existsByQuestionAndAuthor(Question question, User author);
}
