package ericarfs.socialmedia.mapper;

import ericarfs.socialmedia.dto.request.question.CreateQuestionDTO;
import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.entity.Question;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-16T23:44:12-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Ubuntu)"
)
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Override
    public Question toEntity(CreateQuestionDTO createQuestionDTO) {
        if ( createQuestionDTO == null ) {
            return null;
        }

        Question question = new Question();

        question.setBody( createQuestionDTO.body() );
        question.setAnon( createQuestionDTO.anon() );

        return question;
    }

    @Override
    public List<QuestionResponseDTO> listEntityToListDTO(Iterable<Question> question) {
        if ( question == null ) {
            return null;
        }

        List<QuestionResponseDTO> list = new ArrayList<QuestionResponseDTO>();
        for ( Question question1 : question ) {
            list.add( toResponseDTO( question1 ) );
        }

        return list;
    }

    @Override
    public QuestionResponseDTO toResponseDTO(Question question) {
        if ( question == null ) {
            return null;
        }

        Long id = null;
        String body = null;

        id = question.getId();
        body = question.getBody();

        String sentBy = getSentByUser(question);
        String timeCreation = ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(question.getCreatedAt());

        QuestionResponseDTO questionResponseDTO = new QuestionResponseDTO( id, sentBy, body, timeCreation );

        return questionResponseDTO;
    }
}
