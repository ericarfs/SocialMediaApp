package ericarfs.socialmedia.dto.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnswerRequestDTO(
                @NotBlank(message = "Body is required.") @NotNull(message = "Body is required.") @NotEmpty(message = "Body is required.") @Size(min = 4, message = "Answer must have at least 4 characters.") @Size(max = 2048, message = "Answer must have up to 2048 characters.") String body) {
}
