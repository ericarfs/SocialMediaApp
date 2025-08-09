package ericarfs.socialmedia.dto.request.answer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAnswerDTO(
        @NotNull Long question,
        @NotNull @NotBlank @NotEmpty String body,
        @NotNull @NotBlank @NotEmpty String author) {
}
