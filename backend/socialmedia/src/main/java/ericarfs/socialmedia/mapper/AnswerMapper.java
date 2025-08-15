package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.answer.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.answer.ShareResponseDTO;
import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Question;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface AnswerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "shares", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "author", ignore = true)
    Answer toEntity(AnswerRequestDTO createAnswerDTO);

    @Mapping(target = "likesCount", expression = "java(getLikesCount(answer))")
    @Mapping(target = "hasUserLiked", source = "answer", qualifiedByName = "hasUserLiked")
    LikeResponseDTO toLikeResponseDTO(Answer answer);

    @Mapping(target = "sharesCount", expression = "java(getSharesCount(answer))")
    @Mapping(target = "hasUserShared", source = "answer", qualifiedByName = "hasUserShared")
    ShareResponseDTO toShareResponseDTO(Answer answer);

    @Mapping(target = "question.timeCreation", expression = "java(getTimeCreation(question))")
    @Mapping(target = "timeCreation", expression = "java(getTimeCreation(answer))")
    @Mapping(target = "likesInfo", source = "answer")
    @Mapping(target = "sharesInfo", source = "answer")
    AnswerResponseDTO toResponseDTO(Answer answer);

    List<AnswerResponseDTO> listEntityToListDTO(Iterable<Answer> answers);

    default String getTimeCreation(Question question) {
        return question.getFormattedCreationDate();
    }

    default String getTimeCreation(Answer answer) {
        return answer.getFormattedCreationDate();
    }

    default int getLikesCount(Answer answer) {
        return answer.getLikesCount();
    }

    default int getSharesCount(Answer answer) {
        return answer.getSharesCount();
    }
}
