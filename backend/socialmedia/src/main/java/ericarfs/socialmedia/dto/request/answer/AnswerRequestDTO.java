package ericarfs.socialmedia.dto.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AnswerRequestDTO(
        @NotBlank(message = "Body is required.") @NotNull(message = "Body is required.") @NotEmpty(message = "Body is required.") String body) {
}
