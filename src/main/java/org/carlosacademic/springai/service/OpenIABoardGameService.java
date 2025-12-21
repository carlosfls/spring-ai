package org.carlosacademic.springai.service;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenIABoardGameService implements BoardGameService{

    private final ChatClient chatClient;

    public OpenIABoardGameService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public Answer askQuestion(Question question) {
        var answer = chatClient.prompt()
                .user(question.question())
                .call()
                .content();

        return new Answer(answer);
    }
}
