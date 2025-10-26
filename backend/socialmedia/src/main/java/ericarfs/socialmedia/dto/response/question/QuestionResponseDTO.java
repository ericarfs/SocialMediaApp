package ericarfs.socialmedia.dto.response.question;

import ericarfs.socialmedia.dto.response.user.UserBasicDTO;

public record QuestionResponseDTO(
                Long id,
                UserBasicDTO sentBy,
                String body,
                String timeCreation) {
}
