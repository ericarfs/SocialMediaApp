package ericarfs.socialmedia.dto.response.answer;

import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;

public record AnswerBasicDTO(
        Long id,
        QuestionResponseDTO question,
        String body,
        String author,
        String timeCreation,
        AnswerBasicDTO inResponseTo) {
}
