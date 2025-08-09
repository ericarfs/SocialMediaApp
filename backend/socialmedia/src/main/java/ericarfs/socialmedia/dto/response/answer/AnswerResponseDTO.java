package ericarfs.socialmedia.dto.response.answer;

import java.util.List;

import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserListDTO;

public record AnswerResponseDTO(
        String id,
        QuestionResponseDTO question,
        String body,
        String author,
        List<UserListDTO> likes,
        List<UserListDTO> shares) {
}
