package ericarfs.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.User;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
        Optional<Answer> findByIdAndAuthor(Long id, User author);

        List<Answer> findByAuthor(User author);

        @Query(value = "\n" + //
                        "SELECT \n" + //
                        "  a.id, \n" + //
                        "  a.question_id,\n" + //
                        "  a.author_id, \n" + //
                        "  a.body, \n" + //
                        "  a.updated_at, \n" + //
                        "  CASE WHEN a.author_id=:userId THEN a.created_at \n" + //
                        "  ELSE s.share_date END AS created_at \n" + //
                        "FROM answers AS a \n" + //
                        "LEFT JOIN answers_shares AS s ON a.id= s.answer_id AND s.user_id=:userId \n" + //
                        "WHERE a.author_id=:userId OR s.user_id=:userId \n" + //
                        "ORDER BY created_at DESC", nativeQuery = true)
        Page<Answer> findAuthoredAndSharedAnswers(Long userId, Pageable pageable);

        @Query(value = "\n" + //
                        "WITH following AS (\n" + //
                        "  SELECT following_id\n" + //
                        "  FROM users_following\n" + //
                        "  WHERE followers_id = :userId\n" + //
                        ")\n" + //
                        "SELECT\n" + //
                        "  a.id,\n" + //
                        "  a.question_id,\n" + //
                        "  a.author_id,\n" + //
                        "  a.body,\n" + //
                        "  a.updated_at,\n" + //
                        "  CASE\n" + //
                        "    WHEN a.author_id IN (SELECT following_id FROM Following) THEN a.created_at\n" + //
                        "    ELSE s.share_date\n" + //
                        "  END AS created_at\n" + //
                        "FROM\n" + //
                        "  answers AS a\n" + //
                        "  LEFT JOIN answers_shares AS s\n" + //
                        "    ON a.id = s.answer_id AND s.user_id IN (SELECT following_id FROM following)\n" + //
                        "WHERE\n" + //
                        "  a.author_id IN (SELECT following_id FROM following)\n" + //
                        "  OR s.user_id IN (SELECT following_id FROM following)\n" + //
                        "ORDER BY created_at DESC;", nativeQuery = true)
        Page<Answer> findFollowersActivities(Long userId, Pageable pageable);

        boolean existsByIdAndAuthor(Long id, User author);
}
