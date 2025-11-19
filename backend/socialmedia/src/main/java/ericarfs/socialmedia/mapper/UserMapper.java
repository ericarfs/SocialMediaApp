package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ericarfs.socialmedia.dto.request.user.CreateUserDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;
import ericarfs.socialmedia.dto.response.user.UserProfileDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.entity.util.Email;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "icon", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "blockedUsers", ignore = true)
    @Mapping(target = "silencedUsers", ignore = true)
    @Mapping(target = "sentQuestions", ignore = true)
    @Mapping(target = "receivedQuestions", ignore = true)
    @Mapping(target = "answeredQuestions", ignore = true)
    @Mapping(target = "questionHelper", ignore = true)
    @Mapping(target = "allowAnonQuestions", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "resetToken", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "displayName", source = "username")
    User toEntity(CreateUserDTO createUserDTO);

    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "isLoggedUser", source = "username", qualifiedByName = "isLoggedUser")
    @Mapping(target = "isFollowing", source = "followers", qualifiedByName = "isFollowing")
    @Mapping(target = "createdAt", source = "createdAt")
    UserProfileDTO toProfileDTO(User user);

    UserBasicDTO toListDTO(User user);

    List<UserBasicDTO> listEntityToListDTO(Iterable<User> users);

    List<UserResponseDTO> listEntityToResponseListDTO(Iterable<User> users);

    default Email mapEmail(String email) {
        return new Email(email);
    }
}
