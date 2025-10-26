package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerBasicDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.answer.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.answer.ShareResponseDTO;
import ericarfs.socialmedia.entity.Answer;

@Mapper(componentModel = "spring", uses = { MapperHelper.class, QuestionMapper.class })
public interface AnswerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "shares", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "relatedQuestions", ignore = true)
    Answer toEntity(AnswerRequestDTO createAnswerDTO);

    @Mapping(target = "hasUserLiked", source = "answer", qualifiedByName = "hasUserLiked")
    LikeResponseDTO toLikeResponseDTO(Answer answer);

    @Mapping(target = "hasUserShared", source = "answer", qualifiedByName = "hasUserShared")
    ShareResponseDTO toShareResponseDTO(Answer answer);

    @Mapping(target = "question", source = "answer.question")
    @Mapping(target = "timeCreation", expression = "java(ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(answer.getCreatedAt()))")
    @Mapping(target = "likesInfo", source = "answer")
    @Mapping(target = "sharesInfo", source = "answer")
    @Mapping(target = "inResponseTo", source = "answer.question.inResponseToAnswer")
    AnswerResponseDTO toResponseDTO(Answer answer);

    @Mapping(target = "question", source = "answer.question")
    @Mapping(target = "timeCreation", expression = "java(ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(answer.getCreatedAt()))")
    @Mapping(target = "inResponseTo", source = "answer.question.inResponseToAnswer")
    AnswerBasicDTO toBasicDTO(Answer answer);

    List<AnswerResponseDTO> listEntityToListDTO(Iterable<Answer> answers);
}
