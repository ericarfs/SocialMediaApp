package ericarfs.socialmedia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.dto.request.question.CreateQuestionDTO;
import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.exceptions.DatabaseException;
import ericarfs.socialmedia.exceptions.PermissionDeniedException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.QuestionMapper;
import ericarfs.socialmedia.repository.QuestionRepository;
import ericarfs.socialmedia.repository.UserRepository;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionResponseDTO> findAll() {
        return questionMapper.listEntityToListDTO(questionRepository.findAll());
    }

    public QuestionResponseDTO findById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found."));

        return questionMapper.toResponseDTO(question);
    }

    public QuestionResponseDTO create(CreateQuestionDTO createQuestionDTO) {
        User sentBy = userRepository.findByUsername(createQuestionDTO.sentBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        User sentTo = userRepository.findByUsername(createQuestionDTO.sentTo())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (createQuestionDTO.anon() && !sentTo.isAllowAnonQuestions()) {
            throw new PermissionDeniedException("User do not accept anonymous questions.");
        }

        Question question = questionMapper.toEntity(createQuestionDTO);
        question.setSentBy(sentBy);
        question.setSentTo(sentTo);

        try {
            question = questionRepository.save(question);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());

        }

        return questionMapper.toResponseDTO(question);
    }

    public void delete(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found.");
        }
        try {
            questionRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
