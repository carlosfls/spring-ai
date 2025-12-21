package org.carlosacademic.springai.service;

import org.carlosacademic.springai.model.Answer;
import org.carlosacademic.springai.model.Question;

public interface BoardGameService {
    Answer askQuestion(Question question);
}
