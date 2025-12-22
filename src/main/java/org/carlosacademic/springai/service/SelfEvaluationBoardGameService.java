package org.carlosacademic.springai.service;

import org.carlosacademic.springai.exceptions.AnswerNotRelevantException;
import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * Example demonstrating how to validate the response of the AI at runtime
 * In case the response is not relevant to the question, an exception is thrown,
 * and the question is re-asked.
 */
@Service("self-evaluation-board-game-service")
public class SelfEvaluationBoardGameService implements BoardGameService{

    private final ChatClient chatClient;

    //Evaluates the chat response if it is relevant to the question
    private final RelevancyEvaluator relevancyEvaluator;

    public SelfEvaluationBoardGameService(ChatClient.Builder chatClientBuilder){
        var chatOptions = ChatOptions.builder()
                .build();

        this.chatClient = chatClientBuilder
                .defaultOptions(chatOptions)
                .build();

        this.relevancyEvaluator = RelevancyEvaluator.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
    }

    //Retry the question if the response is not relevant for 3 times default
    @Retryable(retryFor = AnswerNotRelevantException.class)
    @Override
    public Answer askQuestion(Question question) {
        var answer = chatClient.prompt()
                .user(question.question())
                .call()
                .content();

        evaluateResponse(question, answer);

        return new Answer(answer);
    }

    @Recover
    public Answer recover(AnswerNotRelevantException exception){
        return new Answer(exception.getMessage());
    }

    private void evaluateResponse(Question question, String answer) {
        EvaluationRequest evaluationRequest = new EvaluationRequest(question.question(), answer);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()){
            throw new AnswerNotRelevantException(question.question(), answer);
        }
    }
}
