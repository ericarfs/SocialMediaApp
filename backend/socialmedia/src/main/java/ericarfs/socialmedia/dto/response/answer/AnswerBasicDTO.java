package ericarfs.socialmedia.dto.response.answer;

import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;

public record AnswerBasicDTO(
                Long id,
                QuestionResponseDTO question,
                String body,
                UserBasicDTO author,
                String timeCreation,
                AnswerBasicDTO inResponseTo) {
}
