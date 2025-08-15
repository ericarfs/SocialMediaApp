package ericarfs.socialmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;
import ericarfs.socialmedia.dto.request.question.CreateQuestionDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.service.AnswerService;
import ericarfs.socialmedia.service.QuestionService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/questions")
public class QuestionController {
    @Autowired
    public QuestionService questionService;

    @Autowired
    public AnswerService answerService;

    @GetMapping()
    public ResponseEntity<List<QuestionResponseDTO>> findAll() {
        List<QuestionResponseDTO> list = questionService.findAllByUser();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> findById(@PathVariable Long id) {
        QuestionResponseDTO question = questionService.findByIdAndUser(id);
        return ResponseEntity.ok().body(question);
    }

    @PostMapping("/{username}")
    public ResponseEntity<QuestionResponseDTO> create(@PathVariable String username,
            @Valid @RequestBody CreateQuestionDTO requestDto) {
        QuestionResponseDTO response = questionService.create(requestDto, username);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri())
                .body(response);
    }

    @PostMapping("/{id}/answer")
    public ResponseEntity<AnswerResponseDTO> createAnswer(@PathVariable Long id,
            @Valid @RequestBody AnswerRequestDTO requestDto) {
        AnswerResponseDTO response = answerService.create(requestDto, id);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri())
                .body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
