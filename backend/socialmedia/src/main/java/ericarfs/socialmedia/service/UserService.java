package ericarfs.socialmedia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ericarfs.socialmedia.dto.request.user.CreateUserDTO;
import ericarfs.socialmedia.dto.request.user.UpdateUserDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.exceptions.DatabaseException;
import ericarfs.socialmedia.exceptions.ResourceNotFoundException;
import ericarfs.socialmedia.mapper.UserMapper;
import ericarfs.socialmedia.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserResponseDTO> findAll() {
        return userMapper.listEntityToListDTO(userRepository.findAll());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO create(CreateUserDTO createUserDTO) {
        User user = userMapper.toEntity(createUserDTO);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new DatabaseException("ERROR: duplicate key value violates unique constraint 'users_email_key'");
            }

            if (userRepository.existsByUsername(user.getUsername())) {
                throw new DatabaseException(
                        "ERROR: duplicate key value violates unique constraint 'users_username_key'");
            }

        }

        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO update(String username, UpdateUserDTO updateUserDTO) {
        User updateUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (updateUserDTO.username() != null && !username.equals(updateUserDTO.username())) {
            if (userRepository.existsByUsername(updateUserDTO.username())) {
                throw new DatabaseException("Username already taken!");
            }
            updateUser.setUsername(updateUserDTO.username());
        }

        if (updateUserDTO.questionHelper() != null) {
            updateUser.setQuestionHelper(updateUserDTO.questionHelper());
        }
        if (updateUserDTO.allowAnonQuestions() != null) {
            updateUser.setAllowAnonQuestions(updateUserDTO.allowAnonQuestions());
        }
        if (updateUserDTO.icon() != null) {
            updateUser.setIcon(updateUserDTO.icon());
        }

        User saveUser = userRepository.save(updateUser);

        return userMapper.toResponseDTO(saveUser);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found!");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
