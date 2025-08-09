package ericarfs.socialmedia.mapper;

import org.springframework.stereotype.Component;

import ericarfs.socialmedia.entity.User;

@Component
public class MapperHelper {

    public String map(User user) {
        return user.getUsername();
    }

}