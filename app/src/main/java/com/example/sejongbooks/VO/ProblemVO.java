package com.example.sejongbooks.VO;

public class ProblemVO {
    private String problemImageString;
    private int yourAnswer;
    private int problemAnswer;

    public final static boolean RIGHT = true;
    public final static boolean WRONG = false;

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
    public boolean checkCorrect(int yourAnswer) {
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
