package finalproject.mobileappclass.com.captura.Models;

/**
 * Created by Nikhil on 12/4/16.
 */

public class QuizScore
{
    private int quizScore;
    private String timeStamp;
    private String languageOfInterest;

    public QuizScore () {}

    public QuizScore(int quizScore , String timeStamp, String languageOfInterest)
    {
        this.quizScore = quizScore;
        this.timeStamp = timeStamp;
        this.languageOfInterest = languageOfInterest;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLanguageOfInterest() {
        return languageOfInterest;
    }

    public void setLanguageOfInterest(String languageOfInterest) {
        this.languageOfInterest = languageOfInterest;
    }
}
