package ericarfs.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.User;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndAuthor(Long id, User author);

    List<Answer> findByAuthor(User author);

    boolean existsByIdAndAuthor(Long id, User author);
}
