package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.service.AuthService;

@Component
public class MapperHelper {
    @Autowired
    private AuthService authService;

    public String map(User user) {
        return user.getUsername();
    }

    @Named("isLoggedUser")
    public boolean mapIsLoggedUser(String username) {
        User user = authService.getAuthenticatedUser();

        return user.getUsername().equals(username);
    }

    @Named("isFollowing")
    public boolean mapIsFollowing(List<User> followersList) {
        User user = authService.getAuthenticatedUser();

        return followersList.contains(user);
    }

    @Named("hasUserLiked")
    public boolean mapHasUserLiked(Answer answer) {
        User user = authService.getAuthenticatedUser();

        return answer.hasUserLiked(user);
    }

    @Named("hasUserShared")
    public boolean mapHasUserShared(Answer answer) {
        User user = authService.getAuthenticatedUser();

        return answer.hasUserShared(user);
    }
}