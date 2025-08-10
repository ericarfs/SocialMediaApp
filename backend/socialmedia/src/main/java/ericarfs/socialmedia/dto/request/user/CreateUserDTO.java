package ericarfs.socialmedia.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
                @NotBlank(message = "Email is required.") @NotNull(message = "Email is required.") @NotEmpty(message = "Email is required.") @Email String email,
                @NotBlank(message = "Username is required.") @NotNull(message = "Username is required.") @NotEmpty(message = "Username is required.") String username,
                @NotBlank(message = "Password is required.") @NotNull(message = "Password is required.") @NotEmpty(message = "Password is required.") String password) {
}
