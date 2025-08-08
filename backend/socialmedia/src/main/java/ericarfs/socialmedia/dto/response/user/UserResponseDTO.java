package ericarfs.socialmedia.dto.response.user;

import java.util.List;

public record UserResponseDTO(
                Long id,
                String username,
                List<UserListDTO> followers,
                List<UserListDTO> following,
                String questionHelper,
                Boolean allowAnonQuestions) {
}
