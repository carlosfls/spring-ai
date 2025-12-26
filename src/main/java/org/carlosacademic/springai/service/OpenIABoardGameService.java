package org.carlosacademic.springai.service;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service("open-ai-board-game-service")
public class OpenIABoardGameService implements BoardGameService{

    private final ChatClient chatClient;

    /**
     * Using a resource to load the prompt template from the classpath.
     * The resource contains the question and the parameters to be replaced.
     * This way is better than having the prompt hardcoded in the code.
     */
    @Value("classpath:/prompts/questionPromptTemplate.st")
    Resource questionPromptTemplate;

    public OpenIABoardGameService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public Answer askQuestion(Question question) {
        var answer = chatClient.prompt()
                .user(promptUserSpec ->
                        promptUserSpec.text(questionPromptTemplate)
                                .param("title", question.title())
                                .param("question", question.question())
                )
                .call()
                .content();

        return new Answer(question.title(), answer);
    }
}
