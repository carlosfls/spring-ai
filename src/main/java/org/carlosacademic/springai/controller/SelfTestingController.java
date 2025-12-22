package org.carlosacademic.springai.controller;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;
import org.carlosacademic.springai.service.BoardGameService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/self-test")
public class SelfTestingController {

    private final BoardGameService boardGameService;

    public SelfTestingController(@Qualifier(value = "self-evaluation-board-game-service") BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Answer> evaluate(@RequestBody Question question) {
        return ResponseEntity.ok(boardGameService.askQuestion(question));
    }
}
