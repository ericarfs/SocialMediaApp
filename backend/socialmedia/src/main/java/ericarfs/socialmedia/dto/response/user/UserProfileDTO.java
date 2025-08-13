package ericarfs.socialmedia.dto.response.user;

public record UserProfileDTO(
                Long id,
                String displayName,
                String username,
                String bio,
                String questionHelper,
                boolean allowAnonQuestions,
                boolean isLoggedUser) {
}
