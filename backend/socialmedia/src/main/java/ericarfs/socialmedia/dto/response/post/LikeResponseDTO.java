package ericarfs.socialmedia.dto.response.post;

public record LikeResponseDTO(
                int likesCount,
                boolean hasUserLiked) {
}
