package ericarfs.socialmedia.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public UserService userService;

    @Autowired
    public AnswerService answerService;

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
    public ResponseEntity<Map<String, Boolean>> blockUser(@PathVariable String username) {
        boolean isBlocked = userService.blockUser(username);

        Map<String, Boolean> response = Collections.singletonMap("isUserBlocked", isBlocked);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/silences")
    public ResponseEntity<List<UserResponseDTO>> findSilencedUsers() {
        List<UserResponseDTO> silencedUsersList = userService.getSilencedUsers();
        return ResponseEntity.ok().body(silencedUsersList);
    }

    @PostMapping("/silences/{username}")
    public ResponseEntity<Map<String, Boolean>> silenceUser(@PathVariable String username) {
        boolean isSilenced = userService.silenceUser(username);

        Map<String, Boolean> response = Collections.singletonMap("isUserSilenced", isSilenced);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/follows/{username}")
    public ResponseEntity<Map<String, Boolean>> followUser(@PathVariable String username) {
        boolean isFollowing = userService.followUser(username);

        Map<String, Boolean> response = Collections.singletonMap("isUserFollowed", isFollowing);

        return ResponseEntity.ok().body(response);
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
