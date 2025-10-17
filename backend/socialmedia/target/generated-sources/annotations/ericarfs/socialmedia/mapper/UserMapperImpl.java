package ericarfs.socialmedia.mapper;

import ericarfs.socialmedia.dto.request.user.CreateUserDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;
import ericarfs.socialmedia.dto.response.user.UserProfileDTO;
import ericarfs.socialmedia.dto.response.user.UserResponseDTO;
import ericarfs.socialmedia.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-16T23:44:12-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Ubuntu)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private MapperHelper mapperHelper;

    @Override
    public User toEntity(CreateUserDTO createUserDTO) {
        if ( createUserDTO == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( mapEmail( createUserDTO.email() ) );
        user.setDisplayName( createUserDTO.username() );
        user.setUsername( createUserDTO.username() );
        user.setPassword( createUserDTO.password() );

        return user;
    }

    @Override
    public UserResponseDTO toResponseDTO(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String displayName = null;
        String username = null;
        String bio = null;
        String questionHelper = null;
        boolean allowAnonQuestions = false;

        id = user.getId();
        displayName = user.getDisplayName();
        username = user.getUsername();
        bio = user.getBio();
        questionHelper = user.getQuestionHelper();
        allowAnonQuestions = user.isAllowAnonQuestions();

        UserResponseDTO userResponseDTO = new UserResponseDTO( id, displayName, username, bio, questionHelper, allowAnonQuestions );

        return userResponseDTO;
    }

    @Override
    public UserProfileDTO toProfileDTO(User user) {
        if ( user == null ) {
            return null;
        }

        boolean isLoggedUser = false;
        Long id = null;
        String displayName = null;
        String username = null;
        String bio = null;
        String questionHelper = null;
        String followingCount = null;
        String followersCount = null;
        boolean allowAnonQuestions = false;

        isLoggedUser = mapperHelper.mapIsLoggedUser( user.getUsername() );
        id = user.getId();
        displayName = user.getDisplayName();
        username = user.getUsername();
        bio = user.getBio();
        questionHelper = user.getQuestionHelper();
        followingCount = String.valueOf( user.getFollowingCount() );
        followersCount = String.valueOf( user.getFollowersCount() );
        allowAnonQuestions = user.isAllowAnonQuestions();

        UserProfileDTO userProfileDTO = new UserProfileDTO( id, displayName, username, bio, questionHelper, followingCount, followersCount, allowAnonQuestions, isLoggedUser );

        return userProfileDTO;
    }

    @Override
    public UserBasicDTO toListDTO(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String displayName = null;
        String username = null;

        id = user.getId();
        displayName = user.getDisplayName();
        username = user.getUsername();

        UserBasicDTO userBasicDTO = new UserBasicDTO( id, displayName, username );

        return userBasicDTO;
    }

    @Override
    public List<UserBasicDTO> listEntityToListDTO(Iterable<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserBasicDTO> list = new ArrayList<UserBasicDTO>();
        for ( User user : users ) {
            list.add( toListDTO( user ) );
        }

        return list;
    }

    @Override
    public List<UserResponseDTO> listEntityToResponseListDTO(Iterable<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<UserResponseDTO>();
        for ( User user : users ) {
            list.add( toResponseDTO( user ) );
        }

        return list;
    }
}
