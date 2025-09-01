package ericarfs.socialmedia.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.answer.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.answer.ShareResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;
import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.Share;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.exceptions.DatabaseException;
import ericarfs.socialmedia.exceptions.PermissionDeniedException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.AnswerMapper;
import ericarfs.socialmedia.mapper.UserMapper;
import ericarfs.socialmedia.repository.AnswerRepository;
import ericarfs.socialmedia.repository.QuestionRepository;
import ericarfs.socialmedia.repository.ShareRepository;
import ericarfs.socialmedia.repository.UserRepository;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ShareRepository shareRepository;

    private final AnswerMapper answerMapper;
    private final UserMapper userMapper;

    private final AuthService authService;

    public AnswerService(
            AnswerRepository answerRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository,
            ShareRepository shareRepository,
            AnswerMapper answerMapper,
            UserMapper userMapper,
            AuthService authService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.shareRepository = shareRepository;
        this.answerMapper = answerMapper;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    public List<AnswerResponseDTO> findAll() {
        return answerMapper.listEntityToListDTO(answerRepository.findAll());
    }

    public List<AnswerResponseDTO> findAllByUser() {
        return answerMapper.listEntityToListDTO(answerRepository.findByAuthor(authService.getAuthenticatedUser()));
    }

    public List<AnswerResponseDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(answerRepository::findByAuthor)
                .map(answerMapper::listEntityToListDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public List<AnswerResponseDTO> findAnswersAndSharesByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return answerMapper.listEntityToListDTO(answerRepository.findAuthoredAndSharedAnswers(user.getId(), pageable));
    }

    public AnswerResponseDTO findById(Long id) {
        return answerRepository.findById(id)
                .map(answerMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));
    }

    public AnswerResponseDTO findByIdAndUser(Long id) {
        return answerRepository.findByIdAndAuthor(id, authService.getAuthenticatedUser())
                .map(answerMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));
    }

    public AnswerResponseDTO create(AnswerRequestDTO createAnswerDTO, Long questionId) {
        User author = authService.getAuthenticatedUser();

        Question question = questionRepository.findByIdAndSentToAndIsAnsweredFalse(questionId, author)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found."));

        if (!question.getSentTo().getUsername().equals(author.getUsername())) {
            throw new PermissionDeniedException("Invalid operation.");
        }

        if (answerRepository.existsByIdAndAuthor(questionId, author)) {
            throw new DatabaseException("Duplicated answer.");
        }

        question.setAnswered(true);

        Answer answer = answerMapper.toEntity(createAnswerDTO);
        answer.setQuestion(question);
        answer.setAuthor(author);

        try {
            questionRepository.save(question);
            answer = answerRepository.save(answer);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

        return answerMapper.toResponseDTO(answer);
    }

    public AnswerResponseDTO update(AnswerRequestDTO createAnswerDTO, Long answerId) {
        Answer answer = answerRepository.findByIdAndAuthor(answerId, authService.getAuthenticatedUser())
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        answer.setBody(createAnswerDTO.body());
        answer.setUpdatedAt(Instant.now());

        answer = answerRepository.save(answer);

        return answerMapper.toResponseDTO(answer);
    }

    public List<UserBasicDTO> findUsersHasLiked(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        return userMapper.listEntityToListDTO(answer.getLikes());
    }

    public LikeResponseDTO toggleLike(Long id) {
        User user = authService.getAuthenticatedUser();

        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        answer.toggleLike(user);

        answer = answerRepository.save(answer);

        return answerMapper.toLikeResponseDTO(answer);
    }

    public List<UserBasicDTO> findUsersHasShared(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        return userMapper.listEntityToListDTO(shareRepository.findSharedUsersByAnswerId(answer.getId()));
    }

    public ShareResponseDTO toggleShare(Long id) {
        User user = authService.getAuthenticatedUser();

        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        Optional<Share> share = shareRepository.findByAnswerAndUser(answer, user);

        if (share.isPresent()) {
            shareRepository.delete(share.get());
        } else {
            Share newShare = new Share();
            newShare.setAnswer(answer);
            newShare.setUser(user);
            shareRepository.save(newShare);
        }

        return answerMapper.toShareResponseDTO(answer);
    }

    public List<AnswerResponseDTO> findFollowersActivities(Pageable pageable) {
        User user = authService.getAuthenticatedUser();

        return answerMapper.listEntityToListDTO(answerRepository.findFollowersActivities(user.getId(), pageable));
    }

    public void delete(Long id) {
        if (!answerRepository.existsByIdAndAuthor(id, authService.getAuthenticatedUser())) {
            throw new ResourceNotFoundException("Answer not found or does not belong to the user.");
        }
        try {
            answerRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
