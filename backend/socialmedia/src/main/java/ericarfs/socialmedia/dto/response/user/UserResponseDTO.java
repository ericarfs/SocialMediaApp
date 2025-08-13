package ericarfs.socialmedia.dto.response.user;

public record UserResponseDTO(
        Long id,
        String displayName,
        String username,
        String bio,
        String questionHelper,
        boolean allowAnonQuestions) {
}
