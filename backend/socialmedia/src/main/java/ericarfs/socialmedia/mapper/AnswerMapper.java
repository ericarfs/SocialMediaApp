package ericarfs.socialmedia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;

import ericarfs.socialmedia.entity.Answer;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface AnswerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    Answer toEntity(AnswerRequestDTO createAnswerDTO);
}
