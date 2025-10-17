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
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.answer.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.answer.ShareResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;

import ericarfs.socialmedia.service.AnswerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/answers/{id}")
public class AnswerController {
    public final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping
    public ResponseEntity<AnswerResponseDTO> findById(@PathVariable("id") Long id) {
        AnswerResponseDTO answer = answerService.findById(id);
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping("/likes")
    public ResponseEntity<List<UserBasicDTO>> findLikes(@PathVariable("id") Long id) {
        List<UserBasicDTO> users = answerService.findUsersHasLiked(id);
        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/likes")
    public ResponseEntity<LikeResponseDTO> like(@PathVariable("id") Long id) {
        LikeResponseDTO response = answerService.toggleLike(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/shares")
    public ResponseEntity<List<UserBasicDTO>> findShares(@PathVariable("id") Long id) {
        List<UserBasicDTO> users = answerService.findUsersHasShared(id);
        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/shares")
    public ResponseEntity<ShareResponseDTO> share(@PathVariable("id") Long id) {
        ShareResponseDTO response = answerService.toggleShare(id);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping
    public ResponseEntity<AnswerResponseDTO> update(@PathVariable("id") Long id,
            @Valid @RequestBody AnswerRequestDTO requestDto) {
        AnswerResponseDTO answer = answerService.update(requestDto, id);
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
