package org.carlosacademic.springai.service;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OpenIABoardGameServiceTest {

    @Autowired
    @Qualifier("self-evaluation-board-game-service")
    private BoardGameService boardGameService;

    @Autowired
    private ChatClient.Builder chatClient;

    //for evaluating the chat response if it is related to the question
    private RelevancyEvaluator relevancyEvaluator;

    //for evaluating the chat response if it is factually correct if it makes sense
    private FactCheckingEvaluator factCheckingEvaluator;

    @BeforeEach
    void setUp() {
        this.relevancyEvaluator = new RelevancyEvaluator(chatClient);
        this.factCheckingEvaluator = FactCheckingEvaluator.builder(chatClient)
                .build();
    }

    @Test
    void askQuestionAndEvaluateResponse() {
        String text = "What is the sky blue?";
        Question question = new Question(text);

        Answer answer = boardGameService.askQuestion(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(text, answer.answer());

        //evaluating the response
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        assertThat(evaluationResponse.isPass())
                .withFailMessage("The response is not relevant to the question")
                .isTrue();
    }

    //Evaluate if the answer is factually correct
    @Test
    void evaluateFactuality() {
        String text = "What is the sky blue?";
        Question question = new Question(text);

        Answer answer = boardGameService.askQuestion(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(text, answer.answer());

        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

        assertThat(evaluationResponse.isPass())
                .withFailMessage("The response is not factually correct")
                .isTrue();
    }
}