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

    @Autowired
    private AuthService authService;

    public List<QuestionResponseDTO> findAll() {
        return questionMapper.listEntityToListDTO(questionRepository.findAll());
    }

    public List<QuestionResponseDTO> findAllByUser() {
        User user = authService.getAuthenticatedUser();

        return questionMapper.listEntityToListDTO(questionRepository.findBySentToAndIsAnsweredFalse(user));
    }

    public QuestionResponseDTO findById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found."));

        return questionMapper.toResponseDTO(question);
    }

    public QuestionResponseDTO findByIdAndUser(Long id) {
        User user = authService.getAuthenticatedUser();

        Question question = questionRepository.findByIdAndSentToAndIsAnsweredFalse(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found or does not belong to the user."));

        return questionMapper.toResponseDTO(question);
    }

    public void create(CreateQuestionDTO createQuestionDTO, String usernameSentTo) {
        User sentBy = authService.getAuthenticatedUser();

        User sentTo = userRepository.findByUsername(usernameSentTo)
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
    }

    public void delete(Long id) {
        User user = authService.getAuthenticatedUser();

        if (!questionRepository.existsByIdAndSentTo(id, user)) {
            throw new ResourceNotFoundException("Question not found or does not belong to the user.");
        }
        try {
            questionRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
