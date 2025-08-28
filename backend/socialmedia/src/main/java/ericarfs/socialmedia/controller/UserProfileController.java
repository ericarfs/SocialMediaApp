package ericarfs.socialmedia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ericarfs.socialmedia.dto.request.user.UpdateUserDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserProfileDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.service.AnswerService;
import ericarfs.socialmedia.service.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users/me")
public class UserProfileController {
    public final UserService userService;
    public final AnswerService answerService;

    public UserProfileController(
            UserService userService,
            AnswerService answerService) {
        this.userService = userService;
        this.answerService = answerService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> findProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        UserProfileDTO user = userService.findProfile(username);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/blocks")
    public ResponseEntity<List<UserResponseDTO>> findBlockedUsers() {
        List<UserResponseDTO> blockedUsersList = userService.getBlockedUsers();
        return ResponseEntity.ok().body(blockedUsersList);
    }

    @PostMapping("/blocks/{username}")
    public ResponseEntity<Void> blockUser(@PathVariable String username) {
        userService.blockUser(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/blocks/{username}")
    public ResponseEntity<Void> unblockUser(@PathVariable String username) {
        userService.unblockUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/silences")
    public ResponseEntity<List<UserResponseDTO>> findSilencedUsers() {
        List<UserResponseDTO> silencedUsersList = userService.getSilencedUsers();
        return ResponseEntity.ok().body(silencedUsersList);
    }

    @PostMapping("/silences/{username}")
    public ResponseEntity<Void> silenceUser(@PathVariable String username) {
        userService.silenceUser(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/silences/{username}")
    public ResponseEntity<Void> unsilenceUser(@PathVariable String username) {
        userService.unsilenceUser(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/follows/{username}")
    public ResponseEntity<Void> followUser(@PathVariable String username) {
        userService.followUser(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/follows/{username}")
    public ResponseEntity<Map<String, Boolean>> unfollowUser(@PathVariable String username) {
        userService.unfollowUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<List<AnswerResponseDTO>> findFollowersActivities() {
        List<AnswerResponseDTO> list = answerService.findFollowersActivities();
        return ResponseEntity.ok().body(list);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDTO> update(@Valid @RequestBody UpdateUserDTO requestDto) {
        UserResponseDTO user = userService.update(requestDto);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        UserResponseDTO user = userService.findByUsername(username);
        userService.delete(user.id());

        return ResponseEntity.noContent().build();
    }

}
