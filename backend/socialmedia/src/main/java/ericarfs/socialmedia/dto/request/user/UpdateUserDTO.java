package ericarfs.socialmedia.dto.request.user;

public record UpdateUserDTO(
        String username,
        String icon,
        String questionHelper,
        Boolean allowAnonQuestions) {
}
