package ericarfs.socialmedia.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.dto.request.question.CreateQuestionDTO;
import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.exceptions.DatabaseException;
import ericarfs.socialmedia.exceptions.PermissionDeniedException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.QuestionMapper;
import ericarfs.socialmedia.repository.AnswerRepository;
import ericarfs.socialmedia.repository.QuestionRepository;
import ericarfs.socialmedia.repository.UserRepository;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    private final QuestionMapper questionMapper;

    private final AuthService authService;

    public QuestionService(
            QuestionRepository questionRepository,
            UserRepository userRepository,
            AnswerRepository answerRepository,
            QuestionMapper questionMapper,
            AuthService authService) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.questionMapper = questionMapper;
        this.authService = authService;
    }

    public List<QuestionResponseDTO> findAll() {
        return questionMapper.listEntityToListDTO(questionRepository.findAll());
    }

    public List<QuestionResponseDTO> findAllByUser(Pageable pageable) {
        return questionMapper.listEntityToListDTO(
                questionRepository.findBySentToAndIsAnsweredFalse(
                        authService.getAuthenticatedUser(), pageable));
    }

    public QuestionResponseDTO findById(Long id) {
        return questionRepository.findById(id)
                .map(questionMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found."));
    }

    public QuestionResponseDTO findByIdAndUser(Long id) {
        return questionRepository.findByIdAndSentToAndIsAnsweredFalse(id, authService.getAuthenticatedUser())
                .map(questionMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found or does not belong to the user."));
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

    public void create(CreateQuestionDTO createQuestionDTO, Long answerId) {
        User sentBy = authService.getAuthenticatedUser();

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        User sentTo = answer.getAuthor();

        if (createQuestionDTO.anon() && !sentTo.isAllowAnonQuestions()) {
            throw new PermissionDeniedException("User do not accept anonymous questions.");
        }

        Question question = questionMapper.toEntity(createQuestionDTO);
        question.setSentBy(sentBy);
        question.setSentTo(sentTo);
        question.setInResponseToAnswer(answer);

        try {
            question = questionRepository.save(question);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());

        }
    }

    public void delete(Long id) {
        if (!questionRepository.existsByIdAndSentTo(id, authService.getAuthenticatedUser())) {
            throw new ResourceNotFoundException("Question not found or does not belong to the user.");
        }
        try {
            questionRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
