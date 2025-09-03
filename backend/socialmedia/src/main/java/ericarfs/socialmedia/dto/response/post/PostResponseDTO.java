package ericarfs.socialmedia.dto.response.post;

import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;

public record PostResponseDTO(
                String id,
                QuestionResponseDTO question,
                String answer,
                String author,
                String timeCreation,
                LikeResponseDTO likesInfo,
                ShareResponseDTO sharesInfo) {
}
