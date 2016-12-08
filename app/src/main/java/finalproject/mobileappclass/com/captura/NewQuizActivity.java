package finalproject.mobileappclass.com.captura;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import finalproject.mobileappclass.com.captura.DatabaseHelper.CapturaDatabaseHelper;
import finalproject.mobileappclass.com.captura.Models.TranslationRequest;
import finalproject.mobileappclass.com.captura.SharedPreferencesHelper.PrefSingleton;

public class NewQuizActivity extends AppCompatActivity {

    public static final String KEY_QUIZ_SCORE = "Quiz Score";
    private int quizQuestionNumber = 1;
    private TextView questionNumberTextView;
    private TextView topToLanguageTextView;
    private TextView bottomToLanguageTextView;
    private TextView englishWordTextView;
    private EditText translationEditText;
    private Button submitButton;

    String currentLanguage;
    ArrayList<TranslationRequest> translationList;
    CapturaDatabaseHelper capturaDatabaseHelper;
    private int numCorrectTranslations = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);

        questionNumberTextView = (TextView) findViewById(R.id.newQuizQuestionNumberTextView);
        topToLanguageTextView = (TextView) findViewById(R.id.newQuizToLanguageTextViewTop);
        bottomToLanguageTextView = (TextView) findViewById(R.id.newQuizToLanguageTextViewBottom);
        englishWordTextView = (TextView) findViewById(R.id.newQuizEnglishWordTextView);
        translationEditText = (EditText) findViewById(R.id.newQuizTranslationEditText);
        submitButton = (Button) findViewById(R.id.newQuizSubmitButton);

        currentLanguage = PrefSingleton.getInstance().readPreference("language"); //get current language from SharedPreferences
        Log.v("NewQuiz", "Current lang: " + currentLanguage);
        capturaDatabaseHelper = CapturaDatabaseHelper.getInstance(getApplicationContext());
        Log.v("NewQuiz", "Got the DB instance");
        translationList = capturaDatabaseHelper.findTranslationRequestsByLanguage(currentLanguage);
        Log.v("New Quiz", "List view contents for " + currentLanguage + "; ");
        for (TranslationRequest req: translationList) {
            Log.v("New Quiz", req.getInputWord() + "->" + req.getTranslatedWord() + ", " + req.getLanguageOfInterest());
        }

        updateLayoutFields();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validate the inputted answer
                String enteredTranslation = translationEditText.getText().toString();
                //Query the database for the correct translation
                String correctTranslation = capturaDatabaseHelper.findTranslationForInputWord(englishWordTextView.getText().toString(), currentLanguage);
                if (correctTranslation.equalsIgnoreCase(enteredTranslation)) {
                    Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
                    numCorrectTranslations++;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_LONG).show();
                    Log.v("NewQuiz", "Correct translation: " + correctTranslation + ", entered: " + enteredTranslation);
                }
                if (quizQuestionNumber > 10) {
                    Intent quizCompleteIntent = new Intent();
                    quizCompleteIntent.putExtra(KEY_QUIZ_SCORE, numCorrectTranslations);
                    setResult(Activity.RESULT_OK, quizCompleteIntent);
                    finish();
                }

                updateLayoutFields();
            }
        });

    }

    public void updateLayoutFields() {
        questionNumberTextView.setText(quizQuestionNumber + "/10");
        quizQuestionNumber++;

        //Randomly select a previous translation from the database of the target language
        int max = translationList.size();
        Log.v("NewQuiz", "Size of list of words: " + max);
        int min = 0;
        Random randomGenerator = new Random();
        int index  = randomGenerator.nextInt(max - min) + min;
        TranslationRequest selectedTranslation = translationList.get(index);
        String englishWord = selectedTranslation.getInputWord();
        String translatedWord = selectedTranslation.getTranslatedWord();

        topToLanguageTextView.setText(currentLanguage);
        bottomToLanguageTextView.setText(currentLanguage);
        englishWordTextView.setText(englishWord);
        translationEditText.setText(translatedWord);
    }

}
