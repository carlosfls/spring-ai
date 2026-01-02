package org.carlosacademic.springai.service;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service("open-ai-board-game-service")
public class OpenIABoardGameService implements BoardGameService{

    private final Logger logger = LoggerFactory.getLogger(OpenIABoardGameService.class);
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
     * Adding the options for the chat prompt.
     * The entity method is used to convert the response into an Answer object.
     * Not all the models do the conversion.
     */
    @Override
    public Answer askQuestion(Question question) {
        var rules = rulesService.getRulesFor(question.title());

        var chatOptions = ChatOptions.builder()
                .temperature(0.7)
                .build();

        //The response entity has the response chat with metadata and the entity object
        ResponseEntity<ChatResponse, Answer> responseEntity = chatClient.prompt()
                .system(promptSystemSpec ->
                        promptSystemSpec.text(promptTemplate)
                                .param("title", question.title())
                                .param("rules", rules)
                )
                .user(question.question())
                .options(chatOptions)
                .call()
                .responseEntity(Answer.class);

        var response = responseEntity.response();

        //getting the metadata from the response
        if(response != null){
            ChatResponseMetadata metadata = response.getMetadata();
            logUsage(metadata.getUsage());
        }

        return responseEntity.entity();

    }

    /**
     * Logging the usage of the API.
     */
    private void logUsage(Usage usage){
        logger.info("Total tokens used: {}",usage.getTotalTokens());
        logger.info("Prompt tokens used: {}",usage.getPromptTokens());
        logger.info("Completion tokens used: {}",usage.getCompletionTokens());
    }
}
