package org.carlosacademic.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopSongsOpenIAService {

    @Value("classpath:/prompts/top-songs-prompt.st")
    Resource promptTemplate;

    private final ChatClient chatClient;

    public TopSongsOpenIAService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    /**
     * Parsing the response to a list of strings
     */
    public List<String> getSongsByYear(String year){
        return chatClient.prompt()
                .user(promptUserSpec ->
                        promptUserSpec.text(promptTemplate)
                                .param("year",year)

                )
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }
 }
