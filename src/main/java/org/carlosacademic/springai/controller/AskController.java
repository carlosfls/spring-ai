package org.carlosacademic.springai.controller;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.carlosacademic.springai.service.BoardGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    private final BoardGameService boardGameService;

    public AskController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping("/ask")
    public ResponseEntity<Answer> askQuestion(@RequestBody Question question) {
        return ResponseEntity.ok(boardGameService.askQuestion(question));
    }
}
