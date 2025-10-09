package ericarfs.socialmedia.service;

import java.time.Instant;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.dto.request.user.CreateUserDTO;
import ericarfs.socialmedia.dto.request.user.UpdateUserDTO;
import ericarfs.socialmedia.dto.response.user.UserProfileDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.entity.enums.Role;
import ericarfs.socialmedia.entity.util.Email;
import ericarfs.socialmedia.exceptions.DatabaseException;
import ericarfs.socialmedia.exceptions.PermissionDeniedException;
import ericarfs.socialmedia.exceptions.ResourceConflictException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.UserMapper;
import ericarfs.socialmedia.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final AuthService authService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    public List<UserResponseDTO> findAll() {
        return userMapper.listEntityToResponseListDTO(userRepository.findAll());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public UserResponseDTO findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public UserResponseDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public UserProfileDTO findProfile(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toProfileDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public String create(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(new Email(createUserDTO.email()))) {
            throw new ResourceConflictException("Email already taken.");
        }

        if (userRepository.existsByUsername(createUserDTO.username())) {
            throw new ResourceConflictException("Username already taken.");
        }

        User user = createUser(createUserDTO);

        user = userRepository.save(user);

        return user.getUsername();
    }

    public User createUser(CreateUserDTO createUserDTO) {
        User user = userMapper.toEntity(createUserDTO);

        String encryptedPassword = passwordEncoder.encode(createUserDTO.password());
        user.setPassword(encryptedPassword);
        user.setRole(Role.BASIC);

        return user;
    }

    public UserResponseDTO update(UpdateUserDTO updateUserDTO) {
        User user = authService.getAuthenticatedUser();

        if (updateUserDTO.username() != null && !user.getUsername().equals(updateUserDTO.username())) {
            if (userRepository.existsByUsername(updateUserDTO.username())) {
                throw new ResourceConflictException("Username already taken.");
            }
            user.setUsername(updateUserDTO.username());
        }

        if (updateUserDTO.displayName() != null) {
            user.setDisplayName(updateUserDTO.displayName());
        }
        if (updateUserDTO.bio() != null) {
            user.setBio(updateUserDTO.bio());
        }
        if (updateUserDTO.questionHelper() != null) {
            user.setQuestionHelper(updateUserDTO.questionHelper());
        }
        if (updateUserDTO.allowAnonQuestions() != null) {
            user.setAllowAnonQuestions(updateUserDTO.allowAnonQuestions());
        }
        if (updateUserDTO.icon() != null) {
            user.setIcon(updateUserDTO.icon());
        }

        user.setUpdatedAt(Instant.now());
        user = userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }

    public List<UserResponseDTO> getFollowers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.listEntityToResponseListDTO(user.getFollowers());
    }

    public List<UserResponseDTO> getFollowing(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.listEntityToResponseListDTO(user.getFollowing());
    }

    public List<UserResponseDTO> getBlockedUsers() {
        User user = authService.getAuthenticatedUser();

        return userMapper.listEntityToResponseListDTO(user.getBlockedUsers());
    }

    public List<UserResponseDTO> getSilencedUsers() {
        User user = authService.getAuthenticatedUser();

        return userMapper.listEntityToResponseListDTO(user.getSilencedUsers());
    }

    public void followUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToFollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (user.getUsername().equals(username)) {
            throw new PermissionDeniedException("You can not follow yourself.");
        }

        if (userToFollow.getBlockedUsers().contains(user)) {
            throw new PermissionDeniedException("You are blocked by this user.");
        }

        if (user.getBlockedUsers().contains(userToFollow)) {
            throw new PermissionDeniedException("This user is blocked.");
        }

        user.follow(userToFollow);

        userRepository.save(user);
    }

    public void unfollowUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToUnfollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.unfollow(userToUnfollow);

        userRepository.save(user);
    }

    public void blockUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToBlock = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (user.getUsername().equals(username)) {
            throw new PermissionDeniedException("You can not block yourself.");
        }

        user.block(userToBlock);

        userRepository.save(user);
    }

    public void unblockUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToUnblock = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.unblock(userToUnblock);

        userRepository.save(user);
    }

    public void silenceUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToSilence = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (user.getUsername().equals(username)) {
            throw new PermissionDeniedException("You can not silence yourself.");
        }

        user.silence(userToSilence);

        userRepository.save(user);
    }

    public void unsilenceUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToUnsilence = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.silence(userToUnsilence);

        userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found.");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
