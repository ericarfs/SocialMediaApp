package ericarfs.socialmedia.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
        @NotBlank @NotNull @NotEmpty @Email String email,
        @NotBlank @NotNull @NotEmpty String username,
        @NotBlank @NotNull @NotEmpty String password) {
}
