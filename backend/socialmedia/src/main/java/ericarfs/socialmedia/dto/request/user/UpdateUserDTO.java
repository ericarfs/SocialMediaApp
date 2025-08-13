package ericarfs.socialmedia.dto.request.user;

public record UpdateUserDTO(
                String displayName,
                String username,
                String bio,
                String icon,
                String questionHelper,
                Boolean allowAnonQuestions) {
}
