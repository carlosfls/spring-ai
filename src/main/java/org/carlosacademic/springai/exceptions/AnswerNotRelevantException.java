package org.carlosacademic.springai.exceptions;

public class AnswerNotRelevantException extends RuntimeException {
    public AnswerNotRelevantException(String message, String answer) {
        super("The answer '" + answer + "' is not relevant to the question '" + message + "'");
    }
}
