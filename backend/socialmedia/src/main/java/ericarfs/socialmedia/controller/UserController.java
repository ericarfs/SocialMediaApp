package ericarfs.socialmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserProfileDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.service.AnswerService;
import ericarfs.socialmedia.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users/{username}")
public class UserController {
    @Autowired
    public UserService userService;

    @Autowired
    public AnswerService answerService;

    @GetMapping
    public ResponseEntity<UserResponseDTO> findByUsername(@PathVariable String username) {
        UserResponseDTO user = userService.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> findProfile(@PathVariable String username) {
        UserProfileDTO user = userService.findProfile(username);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/answers")
    public ResponseEntity<List<AnswerResponseDTO>> findAnswers(@PathVariable String username) {
        List<AnswerResponseDTO> list = answerService.findByUsername(username);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/activities")
    public ResponseEntity<List<AnswerResponseDTO>> findActivities(@PathVariable String username) {
        List<AnswerResponseDTO> list = answerService.findAnswersAndSharesByUser(username);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserResponseDTO>> findFollowers(@PathVariable String username) {
        List<UserResponseDTO> followersList = userService.getFollowers(username);
        return ResponseEntity.ok().body(followersList);
    }

    @GetMapping("/follows")
    public ResponseEntity<List<UserResponseDTO>> findFollowing(@PathVariable String username) {
        List<UserResponseDTO> followingList = userService.getFollowing(username);
        return ResponseEntity.ok().body(followingList);
    }
}
