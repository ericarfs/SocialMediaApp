package ericarfs.socialmedia.dto.response.answer;

public record LikeResponseDTO(
        int likesCount,
        boolean hasUserLiked) {
}
