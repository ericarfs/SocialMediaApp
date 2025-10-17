package ericarfs.socialmedia.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
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
    public final UserService userService;
    public final AnswerService answerService;

    public UserController(
            UserService userService,
            AnswerService answerService) {
        this.userService = userService;
        this.answerService = answerService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> findByUsername(@PathVariable("username") String username) {
        UserResponseDTO user = userService.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> findProfile(@PathVariable("username") String username) {
        UserProfileDTO user = userService.findProfile(username);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/answers")
    public ResponseEntity<List<AnswerResponseDTO>> findAnswers(@PathVariable("username") String username) {
        List<AnswerResponseDTO> list = answerService.findByUsername(username);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/activities")
    public ResponseEntity<PagedModel<AnswerResponseDTO>> findActivities(@PathVariable("username") String username,
            Pageable pageable) {
        Page<AnswerResponseDTO> answers = answerService.findAnswersAndSharesByUser(username, pageable);

        PagedModel<AnswerResponseDTO> pagedAnswers = new PagedModel<>(answers);
        return ResponseEntity.ok().body(pagedAnswers);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<UserResponseDTO>> findFollowers(@PathVariable("username") String username) {
        List<UserResponseDTO> followersList = userService.getFollowers(username);
        return ResponseEntity.ok().body(followersList);
    }

    @GetMapping("/follows")
    public ResponseEntity<List<UserResponseDTO>> findFollowing(@PathVariable("username") String username) {
        List<UserResponseDTO> followingList = userService.getFollowing(username);
        return ResponseEntity.ok().body(followingList);
    }
}
