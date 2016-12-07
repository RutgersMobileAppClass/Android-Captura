package finalproject.mobileappclass.com.captura.Models;

/**
 * Created by Nikhil on 12/4/16.
 */

public class TranslationRequest
{

    private String inputWord;
    private String translatedWord;
    private String languageOfInterest;

    public TranslationRequest () {}

    public TranslationRequest(String inputWord, String translatedWord, String languageOfInterest)
    {
        this.inputWord = inputWord;
        this.translatedWord = translatedWord;
        this.languageOfInterest = languageOfInterest;
    }

    public String getInputWord() {
        return inputWord;
    }

    public void setInputWord(String inputWord) {
        this.inputWord = inputWord;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public String getLanguageOfInterest() {
        return languageOfInterest;
    }

    public void setLanguageOfInterest(String languageOfInterest) {
        this.languageOfInterest = languageOfInterest;
    }

}
