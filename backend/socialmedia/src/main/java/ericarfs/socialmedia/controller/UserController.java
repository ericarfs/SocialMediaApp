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
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    public UserService userService;

    @Autowired
    public AnswerService answerService;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> findByUsername(@PathVariable String username) {
        UserResponseDTO user = userService.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfileDTO> findProfile(@PathVariable String username) {
        UserProfileDTO user = userService.findProfile(username);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{username}/answers")
    public ResponseEntity<List<AnswerResponseDTO>> findAnswers(@PathVariable String username) {
        List<AnswerResponseDTO> list = answerService.findByUsername(username);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserResponseDTO>> findFollowers(@PathVariable String username) {
        List<UserResponseDTO> followersList = userService.getFollowers(username);
        return ResponseEntity.ok().body(followersList);
    }

    @GetMapping("/{username}/follows")
    public ResponseEntity<List<UserResponseDTO>> findFollowing(@PathVariable String username) {
        List<UserResponseDTO> followingList = userService.getFollowing(username);
        return ResponseEntity.ok().body(followingList);
    }

    @PatchMapping("/{username}/follows")
    public ResponseEntity<Map<String, Boolean>> followUser(@PathVariable String username) {
        boolean isFollowing = userService.followUser(username);

        Map<String, Boolean> response = Collections.singletonMap("isUserFollowed", isFollowing);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{username}/blocks")
    public ResponseEntity<List<UserResponseDTO>> findBlockedUsers(@PathVariable String username) {
        List<UserResponseDTO> blockedUsersList = userService.getBlockedUsers(username);
        return ResponseEntity.ok().body(blockedUsersList);
    }

    @PatchMapping("/{username}/blocks")
    public ResponseEntity<Map<String, Boolean>> blockUser(@PathVariable String username) {
        boolean isBlocked = userService.blockUser(username);

        Map<String, Boolean> response = Collections.singletonMap("isUserBlocked", isBlocked);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{username}/silences")
    public ResponseEntity<List<UserResponseDTO>> findSilencedUsers(@PathVariable String username) {
        List<UserResponseDTO> silencedUsersList = userService.getSilencedUsers(username);
        return ResponseEntity.ok().body(silencedUsersList);
    }

    @PatchMapping("/{username}/silences")
    public ResponseEntity<Map<String, Boolean>> silenceUser(@PathVariable String username) {
        boolean isSilenced = userService.silenceUser(username);

        Map<String, Boolean> response = Collections.singletonMap("isUserSilenced", isSilenced);

        return ResponseEntity.ok().body(response);
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
