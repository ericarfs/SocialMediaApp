package ericarfs.socialmedia.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotBlank(message = "Username is required.") @NotNull(message = "Username is required.") @NotEmpty(message = "Username is required.") String username,
        @NotBlank(message = "Password is required.") @NotNull(message = "Password is required.") @NotEmpty(message = "Password is required.") String password) {

}
