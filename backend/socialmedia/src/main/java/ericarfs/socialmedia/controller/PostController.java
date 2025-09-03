package ericarfs.socialmedia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;
import ericarfs.socialmedia.dto.response.post.PostResponseDTO;
import ericarfs.socialmedia.dto.response.post.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.post.ShareResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;

import ericarfs.socialmedia.service.AnswerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts/{id}")
public class PostController {
    public final AnswerService answerService;

    public PostController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping
    public ResponseEntity<PostResponseDTO> findById(@PathVariable Long id) {
        PostResponseDTO answer = answerService.findById(id);
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping("/likes")
    public ResponseEntity<List<UserBasicDTO>> findLikes(@PathVariable Long id) {
        List<UserBasicDTO> users = answerService.findUsersHasLiked(id);
        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/likes")
    public ResponseEntity<LikeResponseDTO> like(@PathVariable Long id) {
        LikeResponseDTO response = answerService.toggleLike(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/shares")
    public ResponseEntity<List<UserBasicDTO>> findShares(@PathVariable Long id) {
        List<UserBasicDTO> users = answerService.findUsersHasShared(id);
        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/shares")
    public ResponseEntity<ShareResponseDTO> share(@PathVariable Long id) {
        ShareResponseDTO response = answerService.toggleShare(id);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping
    public ResponseEntity<PostResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody AnswerRequestDTO requestDto) {
        PostResponseDTO answer = answerService.update(requestDto, id);
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
