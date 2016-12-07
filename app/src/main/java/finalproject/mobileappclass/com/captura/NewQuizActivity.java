package finalproject.mobileappclass.com.captura;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import finalproject.mobileappclass.com.captura.SharedPreferencesHelper.PrefSingleton;

public class NewQuizActivity extends AppCompatActivity {

    private int quizQuestionNumber = 1;
    private TextView questionNumberTextView;
    private TextView topToLanguageTextView;
    private TextView bottomToLanguageTextView;
    private TextView englishWordTextView;
    private EditText translationEditText;
    private Button submitButton;

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


        //Initialize fields
        String currentLanguage = PrefSingleton.getInstance().readPreference("language"); //get current language
        questionNumberTextView.setText(quizQuestionNumber + "/10");
        topToLanguageTextView.setText(currentLanguage);
        bottomToLanguageTextView.setText(currentLanguage);



    }


}
