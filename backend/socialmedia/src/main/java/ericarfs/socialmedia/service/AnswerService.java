package ericarfs.socialmedia.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;

    public List<AnswerResponseDTO> findAll() {
        return answerMapper.listEntityToListDTO(answerRepository.findAll());
    }

    public List<AnswerResponseDTO> findAllByUser() {
        User user = authService.getAuthenticatedUser();

        return answerMapper.listEntityToListDTO(answerRepository.findByAuthor(user));
    }

    public List<AnswerResponseDTO> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return answerMapper.listEntityToListDTO(answerRepository.findByAuthor(user));
    }

    public List<AnswerResponseDTO> findAnswersAndSharesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return answerMapper.listEntityToListDTO(answerRepository.findAuthoredAndSharedAnswers(user.getId()));
    }

    public AnswerResponseDTO findById(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        return answerMapper.toResponseDTO(answer);
    }

    public AnswerResponseDTO findByIdAndUser(Long id) {
        User user = authService.getAuthenticatedUser();

        Answer answer = answerRepository.findByIdAndAuthor(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        return answerMapper.toResponseDTO(answer);
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
        User author = authService.getAuthenticatedUser();

        Answer answer = answerRepository.findByIdAndAuthor(answerId, author)
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

        return userMapper.listEntityToListDTO(answer.sharedUsers());
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

    public List<AnswerResponseDTO> findFollowersActivities() {
        User user = authService.getAuthenticatedUser();

        return answerMapper.listEntityToListDTO(answerRepository.findFollowersActivities(user.getId()));
    }

    public void delete(Long id) {
        User user = authService.getAuthenticatedUser();

        if (!answerRepository.existsByIdAndAuthor(id, user)) {
            throw new ResourceNotFoundException("Answer not found or does not belong to the user.");
        }
        try {
            answerRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
