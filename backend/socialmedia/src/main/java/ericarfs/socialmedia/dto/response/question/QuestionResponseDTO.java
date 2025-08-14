package ericarfs.socialmedia.dto.response.question;

public record QuestionResponseDTO(
        Long id,
        String sentBy,
        String body,
        String timeCreation) {
}
