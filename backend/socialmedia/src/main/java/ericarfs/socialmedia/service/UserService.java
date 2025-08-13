package ericarfs.socialmedia.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.UserMapper;
import ericarfs.socialmedia.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;

    public List<UserResponseDTO> findAll() {
        return userMapper.listEntityToResponseListDTO(userRepository.findAll());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.toResponseDTO(user);
    }

    public UserProfileDTO findProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.toProfileDTO(user);
    }

    public UserResponseDTO create(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(new Email(createUserDTO.email()))) {
            throw new DatabaseException("Email already taken.");
        }

        if (userRepository.existsByUsername(createUserDTO.username())) {
            throw new DatabaseException("Username already taken.");
        }

        User user = createUser(createUserDTO);

        user = userRepository.save(user);

        return userMapper.toResponseDTO(user);
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
                throw new DatabaseException("Username already taken.");
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

    public List<UserResponseDTO> getBlockedUsers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.listEntityToResponseListDTO(user.getBlockedUsers());
    }

    public List<UserResponseDTO> getSilencedUsers(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.listEntityToResponseListDTO(user.getSilencedUsers());
    }

    @Transactional
    public boolean followUser(String username) {
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

        user.toggleFollow(userToFollow);

        userRepository.save(user);

        return user.getFollowing().contains(userToFollow);
    }

    @Transactional
    public boolean blockUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToBlock = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (user.getUsername().equals(username)) {
            throw new PermissionDeniedException("You can not block yourself.");
        }

        user.toggleBlock(userToBlock);

        userRepository.save(user);

        return user.getBlockedUsers().contains(userToBlock);
    }

    @Transactional
    public boolean silenceUser(String username) {
        User user = authService.getAuthenticatedUser();

        User userToSilence = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (user.getUsername().equals(username)) {
            throw new PermissionDeniedException("You can not silence yourself.");
        }

        user.toggleSilence(userToSilence);

        userRepository.save(user);

        return user.getSilencedUsers().contains(userToSilence);
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
