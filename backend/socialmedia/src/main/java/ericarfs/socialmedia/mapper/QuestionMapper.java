package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ericarfs.socialmedia.dto.request.question.CreateQuestionDTO;
import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.dto.response.user.UserBasicDTO;
import ericarfs.socialmedia.entity.Question;
import ericarfs.socialmedia.entity.User;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface QuestionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answered", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "sentTo", ignore = true)
    @Mapping(target = "sentBy", ignore = true)
    @Mapping(target = "inResponseToAnswer", ignore = true)
    Question toEntity(CreateQuestionDTO createQuestionDTO);

    List<QuestionResponseDTO> listEntityToListDTO(Iterable<Question> question);

    @Mapping(target = "sentBy", source = "question", qualifiedByName = "getSentByUser")
    @Mapping(target = "timeCreation", expression = "java(ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(question.getCreatedAt()))")
    QuestionResponseDTO toResponseDTO(Question question);

    @Named("getSentByUser")
    default UserBasicDTO getSentByUser(Question question) {
        if (question.isAnon()) {
            return new UserBasicDTO(null, "Anonymous", "anon");
        }
        return new UserBasicDTO(
                question.getSentBy().getId(),
                question.getSentBy().getDisplayName(),
                question.getSentBy().getUsername());
    }
}
