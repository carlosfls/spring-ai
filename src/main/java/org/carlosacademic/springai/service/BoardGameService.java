package org.carlosacademic.springai.service;

import org.carlosacademic.springai.model.Answer;

public interface BoardGameService {
    Answer askQuestion(String question);
}
