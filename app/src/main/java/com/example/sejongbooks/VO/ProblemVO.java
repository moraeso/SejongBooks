package com.example.sejongbooks.VO;

public class ProblemVO {
    private String problemImageString;
    private int yourAnswer;
    private int problemAnswer;

    public final static int RIGHT = 1;
    public final static int WRONG = 0;

    public ProblemVO(String problemImageString, int problemAnswer){
        this.problemImageString = problemImageString;
        this.problemAnswer = problemAnswer;
    }
    public String getProblemImageString() {
        return problemImageString;
    }
    public void setProblemImageString(String problemImageString) {
        this.problemImageString = problemImageString;
    }
    public int getProblemAnswer() {
        return problemAnswer;
    }
    public void setProblemAnswer(int problemAnswer) {
        this.problemAnswer = problemAnswer;
    }
    public int checkCorrect(int yourAnswer) {
        this.yourAnswer = yourAnswer;

        if(problemAnswer < 10){
            if(problemAnswer == yourAnswer){
                return RIGHT;
            }else{
                return WRONG;
            }
        }else{
            if((problemAnswer/10 == problemAnswer)||(problemAnswer%10 == problemAnswer)){
                return RIGHT;
            }else{
                return WRONG;
            }
        }
    }
}
