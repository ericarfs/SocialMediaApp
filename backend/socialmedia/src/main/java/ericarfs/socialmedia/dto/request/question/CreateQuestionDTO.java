package ericarfs.socialmedia.dto.request.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateQuestionDTO(
        @NotBlank @NotNull @NotEmpty String sentBy,
        @NotBlank @NotNull @NotEmpty String sentTo,
        @NotBlank @NotNull @NotEmpty String body,
        @NotNull boolean anon) {
}
