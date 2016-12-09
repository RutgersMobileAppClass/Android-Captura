package finalproject.mobileappclass.com.captura;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import finalproject.mobileappclass.com.captura.DatabaseHelper.CapturaDatabaseHelper;
import finalproject.mobileappclass.com.captura.Models.QuizScore;
import finalproject.mobileappclass.com.captura.SharedPreferencesHelper.PrefSingleton;

import static finalproject.mobileappclass.com.captura.R.id.bottomBar;

public class QuizActivity extends AppCompatActivity {

    private static final int NEW_QUIZ_REQUEST_CODE = 100;
    private ListView quizListView;
    private QuizAdapter quizAdapter;
    private ArrayList<QuizScore> quizScoreArrayList = new ArrayList<QuizScore>();
    private String currentLanguageName;
    private String currentLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Set adapter for the list view
        quizListView = (ListView) findViewById(R.id.quizListView);
        quizScoreArrayList = CapturaDatabaseHelper.getInstance(getApplicationContext()).getAllScores();
        Log.v("AndroidCaptura", "List size: "+quizScoreArrayList.size());
        quizAdapter = new QuizAdapter(this, quizScoreArrayList);
        quizListView.setAdapter(quizAdapter);

        //Get the currently set language
        currentLanguageName = PrefSingleton.getInstance().readPreference("language_name"); //get current language from SharedPreferences
        currentLanguageCode = PrefSingleton.getInstance().readPreference("language_code");

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_quizzes);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_history) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(intent);
                }

                if (tabId == R.id.tab_photo) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            }
        });

        findViewById(R.id.normal_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newQuizIntent = new Intent(getApplicationContext(), NewQuizActivity.class);
                startActivityForResult(newQuizIntent, NEW_QUIZ_REQUEST_CODE);
            }


        });

    }

    //Invoked on return from taking a quiz.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_QUIZ_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int numCorrectAnswers = data.getIntExtra(NewQuizActivity.KEY_QUIZ_SCORE, -1);
                int totalQuestions = data.getIntExtra(NewQuizActivity.KEY_QUIZ_TOTAL, -1);
                if (numCorrectAnswers > -1)
                {
                    SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy");    //for database (and analytics)
                    String dateFormat = s.format(new Date());   //for display on listview

                    CapturaDatabaseHelper capturaDatabaseHelper = CapturaDatabaseHelper.getInstance(getApplicationContext());
                    QuizScore quizScore = new QuizScore(numCorrectAnswers, dateFormat, currentLanguageCode, totalQuestions, currentLanguageName);
                    capturaDatabaseHelper.insertQuizScore(quizScore);
                    Log.v("AndroidCaptura", "Inserted in database");
                    quizAdapter.add(quizScore);
                    quizAdapter.notifyDataSetChanged();
                }
            }
            else {
                Log.v("AndroidCaptura", "Failed to complete quiz.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_quizactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:

                Intent intent = new Intent(QuizActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}