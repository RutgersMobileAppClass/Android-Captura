package finalproject.mobileappclass.com.captura.Models;

/**
 * Created by Nikhil on 12/4/16.
 */

public class TranslationRequest
{

    private String inputWord;
    private String translatedWord;
    private String languageCode;
    private String languageName;

    public TranslationRequest () {}

    public TranslationRequest(String inputWord, String translatedWord, String languageCode, String languageName)
    {
        this.inputWord = inputWord;
        this.translatedWord = translatedWord;
        this.languageCode = languageCode;
        this.languageName = languageName;
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

}
