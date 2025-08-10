package ericarfs.socialmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ericarfs.socialmedia.dto.request.user.UpdateUserDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.service.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    public UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> listUserByUsername(@PathVariable String username) {
        UserResponseDTO user = userService.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDTO> update(@Valid @RequestBody UpdateUserDTO requestDto) {
        UserResponseDTO user = userService.update(requestDto);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        UserResponseDTO user = userService.findByUsername(username);
        userService.delete(user.id());

        return ResponseEntity.noContent().build();
    }

}
