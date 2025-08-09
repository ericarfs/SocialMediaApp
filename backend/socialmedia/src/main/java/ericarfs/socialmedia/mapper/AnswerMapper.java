package ericarfs.socialmedia.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ericarfs.socialmedia.dto.request.answer.CreateAnswerDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.entity.Answer;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface AnswerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "shares", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "author", ignore = true)
    Answer toEntity(CreateAnswerDTO createAnswerDTO);

    AnswerResponseDTO toResponseDTO(Answer answer);

    List<AnswerResponseDTO> listEntityToListDTO(Iterable<Answer> answers);
}
