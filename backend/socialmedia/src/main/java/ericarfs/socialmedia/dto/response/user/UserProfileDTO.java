package ericarfs.socialmedia.dto.response.user;

import java.time.Instant;

public record UserProfileDTO(
                Long id,
                String icon,
                String displayName,
                String username,
                String bio,
                String questionHelper,
                Instant createdAt,
                int followingCount,
                int followersCount,
                int answersCount,
                boolean allowAnonQuestions,
                boolean isLoggedUser,
                boolean isFollowing) {
}
