package ericarfs.socialmedia.dto.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerDTO(
                @NotNull @NotBlank @NotEmpty String body) {
}
