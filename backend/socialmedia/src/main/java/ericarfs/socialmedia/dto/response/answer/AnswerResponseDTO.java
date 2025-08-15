package ericarfs.socialmedia.dto.response.answer;

import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;

public record AnswerResponseDTO(
                String id,
                QuestionResponseDTO question,
                String body,
                String author,
                String timeCreation,
                LikeResponseDTO likesInfo,
                ShareResponseDTO sharesInfo) {
}
