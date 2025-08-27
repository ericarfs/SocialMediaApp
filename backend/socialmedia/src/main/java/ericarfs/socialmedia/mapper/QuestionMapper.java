package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ericarfs.socialmedia.dto.request.question.CreateQuestionDTO;
import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.entity.Question;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface QuestionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answered", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "sentTo", ignore = true)
    @Mapping(target = "sentBy", ignore = true)
    Question toEntity(CreateQuestionDTO createQuestionDTO);

    List<QuestionResponseDTO> listEntityToListDTO(Iterable<Question> question);

    @Mapping(target = "sentBy", expression = "java(getSentByUser(question))")
    @Mapping(target = "timeCreation", expression = "java(ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(question.getCreatedAt()))")
    QuestionResponseDTO toResponseDTO(Question question);

    default String getSentByUser(Question question) {
        if (question.isAnon()) {
            return "Anonymous";
        }
        return question.getSentBy().getUsername();
    }
}
