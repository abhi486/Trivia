package com.example.trivia.Model;

import androidx.annotation.NonNull;

public class Question {
    private String answer;
    private boolean answertrue;

    public Question() {
    }

    public Question(String answer, boolean answertrue) {
        this.answer = answer;
        this.answertrue = answertrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswertrue() {
        return answertrue;
    }

    public void setAnswertrue(boolean answertrue) {
        this.answertrue = answertrue;
    }

    @NonNull
    @Override
    public String toString() {
        super.toString();
        return "Question{" +
                "Answer = " + answer
                + "AnswerTrue = " + answertrue
                + "}\n";
    }
}
