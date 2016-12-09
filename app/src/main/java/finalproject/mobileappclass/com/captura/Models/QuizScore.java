package finalproject.mobileappclass.com.captura.Models;

/**
 * Created by Nikhil on 12/4/16.
 */

public class QuizScore
{
    private int quizScore;
    private int numQuestions;
    private String timeStamp;
    private String languageCode;
    private String languageName;

    public QuizScore () {}

    public QuizScore(int quizScore , String timeStamp, String languageCode, int numQuestions, String languageName)
    {
        this.quizScore = quizScore;
        this.timeStamp = timeStamp;
        this.languageCode = languageCode;
        this.numQuestions = numQuestions;
        this.languageName = languageName;
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

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }
}
