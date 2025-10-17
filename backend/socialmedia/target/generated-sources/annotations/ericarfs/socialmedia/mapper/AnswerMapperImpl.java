package ericarfs.socialmedia.mapper;

import ericarfs.socialmedia.dto.request.answer.AnswerRequestDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerBasicDTO;
import ericarfs.socialmedia.dto.response.answer.AnswerResponseDTO;
import ericarfs.socialmedia.dto.response.answer.LikeResponseDTO;
import ericarfs.socialmedia.dto.response.answer.ShareResponseDTO;
import ericarfs.socialmedia.dto.response.question.QuestionResponseDTO;
import ericarfs.socialmedia.entity.Answer;
import ericarfs.socialmedia.entity.Question;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-16T23:44:12-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Ubuntu)"
)
@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Autowired
    private MapperHelper mapperHelper;

    @Override
    public Answer toEntity(AnswerRequestDTO createAnswerDTO) {
        if ( createAnswerDTO == null ) {
            return null;
        }

        Answer answer = new Answer();

        answer.setBody( createAnswerDTO.body() );

        return answer;
    }

    @Override
    public LikeResponseDTO toLikeResponseDTO(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        boolean hasUserLiked = false;
        int likesCount = 0;

        hasUserLiked = mapperHelper.mapHasUserLiked( answer );
        likesCount = answer.getLikesCount();

        LikeResponseDTO likeResponseDTO = new LikeResponseDTO( likesCount, hasUserLiked );

        return likeResponseDTO;
    }

    @Override
    public ShareResponseDTO toShareResponseDTO(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        boolean hasUserShared = false;
        int sharesCount = 0;

        hasUserShared = mapperHelper.mapHasUserShared( answer );
        sharesCount = answer.getSharesCount();

        ShareResponseDTO shareResponseDTO = new ShareResponseDTO( sharesCount, hasUserShared );

        return shareResponseDTO;
    }

    @Override
    public AnswerResponseDTO toResponseDTO(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        LikeResponseDTO likesInfo = null;
        ShareResponseDTO sharesInfo = null;
        AnswerBasicDTO inResponseTo = null;
        Long id = null;
        QuestionResponseDTO question = null;
        String body = null;
        String author = null;

        likesInfo = toLikeResponseDTO( answer );
        sharesInfo = toShareResponseDTO( answer );
        inResponseTo = toBasicDTO( answerQuestionInResponseToAnswer( answer ) );
        id = answer.getId();
        question = questionToQuestionResponseDTO( answer.getQuestion() );
        body = answer.getBody();
        author = mapperHelper.map( answer.getAuthor() );

        String timeCreation = ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(answer.getCreatedAt());

        AnswerResponseDTO answerResponseDTO = new AnswerResponseDTO( id, question, body, author, timeCreation, likesInfo, sharesInfo, inResponseTo );

        return answerResponseDTO;
    }

    @Override
    public AnswerBasicDTO toBasicDTO(Answer answer) {
        if ( answer == null ) {
            return null;
        }

        AnswerBasicDTO inResponseTo = null;
        Long id = null;
        QuestionResponseDTO question = null;
        String body = null;
        String author = null;

        inResponseTo = toBasicDTO( answerQuestionInResponseToAnswer( answer ) );
        id = answer.getId();
        question = questionToQuestionResponseDTO( answer.getQuestion() );
        body = answer.getBody();
        author = mapperHelper.map( answer.getAuthor() );

        String timeCreation = ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(answer.getCreatedAt());

        AnswerBasicDTO answerBasicDTO = new AnswerBasicDTO( id, question, body, author, timeCreation, inResponseTo );

        return answerBasicDTO;
    }

    @Override
    public List<AnswerResponseDTO> listEntityToListDTO(Iterable<Answer> answers) {
        if ( answers == null ) {
            return null;
        }

        List<AnswerResponseDTO> list = new ArrayList<AnswerResponseDTO>();
        for ( Answer answer : answers ) {
            list.add( toResponseDTO( answer ) );
        }

        return list;
    }

    private Answer answerQuestionInResponseToAnswer(Answer answer) {
        Question question = answer.getQuestion();
        if ( question == null ) {
            return null;
        }
        return question.getInResponseToAnswer();
    }

    protected QuestionResponseDTO questionToQuestionResponseDTO(Question question) {
        if ( question == null ) {
            return null;
        }

        Long id = null;
        String sentBy = null;
        String body = null;

        id = question.getId();
        sentBy = mapperHelper.map( question.getSentBy() );
        body = question.getBody();

        String timeCreation = ericarfs.socialmedia.utils.TimeUtils.getFormattedCreationDate(question.getCreatedAt());

        QuestionResponseDTO questionResponseDTO = new QuestionResponseDTO( id, sentBy, body, timeCreation );

        return questionResponseDTO;
    }
}
