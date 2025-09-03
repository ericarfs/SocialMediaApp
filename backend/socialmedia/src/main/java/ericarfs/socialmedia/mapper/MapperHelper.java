package ericarfs.socialmedia.mapper;

import java.time.Instant;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Post;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;
import ericarfs.socialmedia.service.AuthService;
import ericarfs.socialmedia.utils.TimeUtils;

@Component
public class MapperHelper {
    @Autowired
    private AuthService authService;

    public String map(User user) {
        return user.getUsername();
    }

    public String map(Question question) {
        return question.getBody();
    }

    public String map(Answer answer) {
        return answer.getBody();
    }

    public String map(Instant instant) {
        return TimeUtils.getFormattedCreationDate(instant);
    }

    @Named("isLoggedUser")
    public boolean mapIsLoggedUser(String username) {
        User user = authService.getAuthenticatedUser();

        return user.getUsername().equals(username);
    }

    @Named("hasUserLiked")
    public boolean mapHasUserLiked(Post post) {
        User user = authService.getAuthenticatedUser();

        return post.hasUserLiked(user);
    }

    @Named("hasUserShared")
    public boolean mapHasUserShared(Post post) {
        User user = authService.getAuthenticatedUser();

        return post.hasUserShared(user);
    }
}