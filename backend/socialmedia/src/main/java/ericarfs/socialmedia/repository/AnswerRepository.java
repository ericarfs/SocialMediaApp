package ericarfs.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
