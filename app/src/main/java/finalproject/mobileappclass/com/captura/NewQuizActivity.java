package finalproject.mobileappclass.com.captura;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    public static final String KEY_QUIZ_TOTAL = "Quiz total";
    private int quizQuestionNumber = 1;
    private int questionCount;

    private TextView questionNumberTextView;
    private TextView topToLanguageTextView;
    private TextView bottomToLanguageTextView;
    private TextView englishWordTextView;
    private EditText translationEditText;
    private Button submitButton;

    private String currentLanguageCode;
    private String currentLanguageName;
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
        capturaDatabaseHelper = CapturaDatabaseHelper.getInstance(getApplicationContext());

        currentLanguageCode = PrefSingleton.getInstance().readPreference("language_code"); //get current language from SharedPreferences
        currentLanguageName = PrefSingleton.getInstance().readPreference("language_name");



        translationList = capturaDatabaseHelper.findTranslationRequestsByLanguage(currentLanguageCode);
        if(translationList.size() == 0)
        {
            Toast.makeText(getApplicationContext(), "No words to quiz you on!", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(translationList.size() > 10)
        {
            questionCount = 10;
            updateLayoutFields();
        }
        else
        {
            questionCount = translationList.size();
            updateLayoutFields();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validate the inputted answer
                String enteredTranslation = translationEditText.getText().toString().trim();
                //Query the database for the correct translation
                String correctTranslation = capturaDatabaseHelper.findTranslationForInputWord(englishWordTextView.getText().toString(), currentLanguageCode);
                if (correctTranslation.equalsIgnoreCase(enteredTranslation)) {
                    Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
                    numCorrectTranslations++;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_LONG).show();
                    Log.v("NewQuiz", "Correct translation: " + correctTranslation + ", entered: " + enteredTranslation);
                }
                if (quizQuestionNumber > questionCount) {
                    Intent quizCompleteIntent = new Intent();
                    quizCompleteIntent.putExtra(KEY_QUIZ_SCORE, numCorrectTranslations);
                    quizCompleteIntent.putExtra(KEY_QUIZ_TOTAL, questionCount);
                    setResult(Activity.RESULT_OK, quizCompleteIntent);
                    finish();
                }

                if(quizQuestionNumber <= questionCount)
                {
                    updateLayoutFields();
                }
            }
        });

    }

    public void updateLayoutFields() {
        questionNumberTextView.setText(quizQuestionNumber + "/" + questionCount);
        quizQuestionNumber++;


        //Randomly select a previous translation from the database of the target language
        int max = translationList.size();
        int min = 0;
        Random randomGenerator = new Random();
        int index  = randomGenerator.nextInt(max - min) + min;
        Log.v("NewQuiz", "Size of list of words: " + max);
        TranslationRequest selectedTranslation = translationList.get(index);
        String englishWord = selectedTranslation.getInputWord();
        englishWordTextView.setText(englishWord);
        translationEditText.getText().clear();
        translationList.remove(index);

        topToLanguageTextView.setText(currentLanguageName);
        bottomToLanguageTextView.setText(currentLanguageName);
    }

}
