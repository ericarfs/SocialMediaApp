package ericarfs.socialmedia.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        @NotBlank(message = "Password is required.") @NotNull(message = "Password is required.") @NotEmpty(message = "Password is required.") @Size(min = 8, message = "Password must have at least 8 characters.") String newPassword) {

}
