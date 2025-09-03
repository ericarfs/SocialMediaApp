package ericarfs.socialmedia.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;
import ericarfs.socialmedia.dto.response.post.PostResponseDTO;
import ericarfs.socialmedia.dto.response.post.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.post.ShareResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;
import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Post;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.Share;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.exceptions.DatabaseException;
import ericarfs.socialmedia.exceptions.PermissionDeniedException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.AnswerMapper;
import ericarfs.socialmedia.mapper.PostMapper;
import ericarfs.socialmedia.mapper.UserMapper;
import ericarfs.socialmedia.repository.AnswerRepository;
import ericarfs.socialmedia.repository.PostRepository;
import ericarfs.socialmedia.repository.QuestionRepository;
import ericarfs.socialmedia.repository.ShareRepository;
import ericarfs.socialmedia.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class AnswerService {
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ShareRepository shareRepository;

    private final AnswerMapper answerMapper;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    private final AuthService authService;

    public AnswerService(
            PostRepository postRepository,
            AnswerRepository answerRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository,
            ShareRepository shareRepository,
            AnswerMapper answerMapper,
            PostMapper postMapper,
            UserMapper userMapper,
            AuthService authService) {
        this.postRepository = postRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.shareRepository = shareRepository;
        this.answerMapper = answerMapper;
        this.postMapper = postMapper;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    public List<PostResponseDTO> findAll() {
        return postMapper.listEntityToListDTO(postRepository.findAll());
    }

    public List<PostResponseDTO> findAllByUser() {
        return postMapper.listEntityToListDTO(postRepository.findByAuthor(authService.getAuthenticatedUser()));
    }

    public List<PostResponseDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(postRepository::findByAuthor)
                .map(postMapper::listEntityToListDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public List<PostResponseDTO> findPostsAndSharesByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return postMapper.listEntityToListDTO(postRepository.findAuthoredAndSharedPosts(user.getId(), pageable));
    }

    public PostResponseDTO findById(Long id) {
        return postRepository.findById(id)
                .map(postMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));
    }

    public PostResponseDTO findByIdAndUser(Long id) {
        return postRepository.findByIdAndAuthor(id, authService.getAuthenticatedUser())
                .map(postMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));
    }

    @Transactional
    public PostResponseDTO create(AnswerRequestDTO createAnswerDTO, Long questionId) {
        User author = authService.getAuthenticatedUser();

        Question question = questionRepository.findByIdAndSentToAndIsAnsweredFalse(questionId, author)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found."));

        if (!question.getSentTo().getUsername().equals(author.getUsername())) {
            throw new PermissionDeniedException("Invalid operation.");
        }

        if (postRepository.existsByIdAndAuthor(questionId, author)) {
            throw new DatabaseException("Duplicated answer.");
        }

        Answer answer = answerMapper.toEntity(createAnswerDTO);
        answer.setQuestion(question);

        answer = answerRepository.save(answer);

        Post post = new Post();
        post.setAnswer(answer);
        post.setAuthor(author);

        question.setAnswered(true);

        try {
            questionRepository.save(question);
            post = postRepository.save(post);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

        return postMapper.toResponseDTO(post);
    }

    public PostResponseDTO update(AnswerRequestDTO createAnswerDTO, Long answerId) {
        Post post = postRepository.findByIdAndAuthor(answerId,
                authService.getAuthenticatedUser())
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found."));

        post.getAnswer().setBody(createAnswerDTO.body());
        post.setUpdatedAt(Instant.now());

        post = postRepository.save(post);

        return postMapper.toResponseDTO(post);
    }

    public List<UserBasicDTO> findUsersHasLiked(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));

        return userMapper.listEntityToListDTO(post.getLikes());
    }

    public LikeResponseDTO toggleLike(Long id) {
        User user = authService.getAuthenticatedUser();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));

        post.toggleLike(user);

        post = postRepository.save(post);

        return postMapper.toLikeResponseDTO(post);
    }

    public List<UserBasicDTO> findUsersHasShared(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));

        return userMapper.listEntityToListDTO(shareRepository.findSharedUsersByPostId(post.getId()));
    }

    public ShareResponseDTO toggleShare(Long id) {
        User user = authService.getAuthenticatedUser();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));

        Optional<Share> share = shareRepository.findByPostAndUser(post, user);

        if (share.isPresent()) {
            shareRepository.delete(share.get());
        } else {
            Share newShare = new Share();
            newShare.setPost(post);
            newShare.setUser(user);
            shareRepository.save(newShare);
        }

        return postMapper.toShareResponseDTO(post);
    }

    public List<PostResponseDTO> findFollowersActivities(Pageable pageable) {
        User user = authService.getAuthenticatedUser();

        return postMapper.listEntityToListDTO(postRepository.findFollowersActivities(user.getId(), pageable));
    }

    public void delete(Long id) {
        if (!postRepository.existsByIdAndAuthor(id, authService.getAuthenticatedUser())) {
            throw new ResourceNotFoundException("Post not found or does not belong to the user.");
        }
        try {
            postRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
