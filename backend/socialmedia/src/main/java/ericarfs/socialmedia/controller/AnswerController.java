package ericarfs.socialmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ericarfs.socialmedia.dto.request.answer.CreateAnswerDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.answer.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.answer.ShareResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserListDTO;

import ericarfs.socialmedia.service.AnswerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    @Autowired
    public AnswerService answerService;

    @GetMapping
    public ResponseEntity<List<AnswerResponseDTO>> listAnswers() {
        List<AnswerResponseDTO> list = answerService.findAllByUser();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> listAnswerById(@PathVariable Long id) {
        AnswerResponseDTO answer = answerService.findById(id);
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserListDTO>> answerLikes(@PathVariable Long id) {
        List<UserListDTO> users = answerService.findUsersHasLiked(id);
        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/{id}/likes")
    public ResponseEntity<LikeResponseDTO> likeAnswer(@PathVariable Long id) {
        LikeResponseDTO response = answerService.toggleLike(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}/shares")
    public ResponseEntity<List<UserListDTO>> answerShares(@PathVariable Long id) {
        List<UserListDTO> users = answerService.findUsersHasShared(id);
        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/{id}/shares")
    public ResponseEntity<ShareResponseDTO> shareAnswer(@PathVariable Long id) {
        ShareResponseDTO response = answerService.toggleShare(id);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody CreateAnswerDTO requestDto) {
        AnswerResponseDTO answer = answerService.update(requestDto, id);
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
