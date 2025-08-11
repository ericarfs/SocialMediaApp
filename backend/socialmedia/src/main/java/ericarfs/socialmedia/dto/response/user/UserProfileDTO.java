package ericarfs.socialmedia.dto.response.user;

import java.util.List;

import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;

public record UserProfileDTO(
        Long id,
        String username,
        List<UserListDTO> followers,
        List<UserListDTO> following,
        List<AnswerResponseDTO> answeredQuestions,
        String questionHelper,
        Boolean allowAnonQuestions) {
}
