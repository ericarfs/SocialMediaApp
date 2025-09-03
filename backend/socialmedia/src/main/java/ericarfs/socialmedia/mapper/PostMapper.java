package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ericarfs.socialmedia.dto.response.post.PostResponseDTO;
import ericarfs.socialmedia.dto.response.post.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.post.ShareResponseDTO;

import ericarfs.socialmedia.entity.Post;

@Mapper(componentModel = "spring", uses = { MapperHelper.class, QuestionMapper.class })
public interface PostMapper {
    @Mapping(target = "hasUserLiked", source = "post", qualifiedByName = "hasUserLiked")
    LikeResponseDTO toLikeResponseDTO(Post post);

    @Mapping(target = "hasUserShared", source = "post", qualifiedByName = "hasUserShared")
    ShareResponseDTO toShareResponseDTO(Post post);

    @Mapping(target = "timeCreation", expression = "java(ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(post.getCreatedAt()))")
    @Mapping(target = "likesInfo", source = "post")
    @Mapping(target = "sharesInfo", source = "post")
    @Mapping(target = "question", source = "answer.question")
    PostResponseDTO toResponseDTO(Post post);

    List<PostResponseDTO> listEntityToListDTO(Iterable<Post> post);
}
