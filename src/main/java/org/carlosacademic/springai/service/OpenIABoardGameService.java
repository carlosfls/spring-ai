package org.carlosacademic.springai.service;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service("open-ai-board-game-service")
public class OpenIABoardGameService implements BoardGameService{

    private final ChatClient chatClient;

    public OpenIABoardGameService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public Answer askQuestion(Question question) {
        String text = """
                Answer the question about:
                The title of the question is {title}.
                The question is: {question}.
                """;
        var answer = chatClient.prompt()
                .user(promptUserSpec ->
                        promptUserSpec.text(text)
                                .param("title", question.title())
                                .param("question", question.question())
                )
                .call()
                .content();

        return new Answer(question.title(), answer);
    }
}
