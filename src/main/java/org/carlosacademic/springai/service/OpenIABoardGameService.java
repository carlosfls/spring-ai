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
    private final ContextService rulesService;

    /**
     * Using a resource to load the prompt template from the classpath.
     * The resource contains the question and the parameters to be replaced.
     * This way is better than having the prompt hardcoded in the code.
     */
    @Value("classpath:/prompts/systemPromptTemplate.st")
    Resource promptTemplate;

    public OpenIABoardGameService(ChatClient.Builder chatClient, ContextService rulesService) {
        this.chatClient = chatClient.build();
        this.rulesService = rulesService;
    }

    /**
     * The prompt accepts a String or a Resource.
     * Using the system option to send the instructions to the LLM.
     * And the user option to send the question to the LLM.
     */
    @Override
    public Answer askQuestion(Question question) {
        var rules = rulesService.getRulesFor(question.title());
        var answer = chatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec.text(promptTemplate)
                                .param("title", question.title())
                                .param("rules", rules)
                )
                .user(question.question())
                .call()
                .content();

        return new Answer(question.title(), answer);
    }
}
