package ericarfs.socialmedia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.dto.request.answer.CreateAnswerDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.exceptions.DatabaseException;
import ericarfs.socialmedia.exceptions.PermissionDeniedException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.AnswerMapper;
import ericarfs.socialmedia.repository.AnswerRepository;
import ericarfs.socialmedia.repository.QuestionRepository;
import ericarfs.socialmedia.repository.UserRepository;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerMapper answerMapper;

    public List<AnswerResponseDTO> findAll() {
        return answerMapper.listEntityToListDTO(answerRepository.findAll());
    }

    public AnswerResponseDTO findById(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        return answerMapper.toResponseDTO(answer);
    }

    public AnswerResponseDTO create(CreateAnswerDTO createAnswerDTO) {
        User author = userRepository.findByUsername(createAnswerDTO.author())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Question question = questionRepository.findById(createAnswerDTO.question())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found."));

        if (!question.getSentTo().getUsername().equals(author.getUsername())) {
            throw new PermissionDeniedException("Invalid operation.");
        }

        Answer answer = answerMapper.toEntity(createAnswerDTO);
        answer.setQuestion(question);
        answer.setAuthor(author);

        try {
            answer = answerRepository.save(answer);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());

        }

        return answerMapper.toResponseDTO(answer);
    }

    public void delete(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Answer not found.");
        }
        try {
            answerRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
