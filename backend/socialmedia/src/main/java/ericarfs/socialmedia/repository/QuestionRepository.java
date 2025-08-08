package ericarfs.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
