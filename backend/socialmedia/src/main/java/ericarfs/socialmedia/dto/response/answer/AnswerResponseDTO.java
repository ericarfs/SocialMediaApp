package ericarfs.socialmedia.dto.response.answer;

import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;

public record AnswerResponseDTO(
        Long id,
        QuestionResponseDTO question,
        String body,
        UserBasicDTO author,
        String timeCreation,
        LikeResponseDTO likesInfo,
        ShareResponseDTO sharesInfo,
        AnswerBasicDTO inResponseTo) {
}
