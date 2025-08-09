package ericarfs.socialmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ericarfs.socialmedia.dto.request.answer.CreateAnswerDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.service.AnswerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {
    @Autowired
    public AnswerService answerService;

    @GetMapping
    public ResponseEntity<List<AnswerResponseDTO>> listAnswers() {
        List<AnswerResponseDTO> list = answerService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDTO> listAnswerById(@PathVariable Long id) {
        AnswerResponseDTO answer = answerService.findById(id);
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping
    public ResponseEntity<AnswerResponseDTO> create(@Valid @RequestBody CreateAnswerDTO requestDto) {
        AnswerResponseDTO response = answerService.create(requestDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri())
                .body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
