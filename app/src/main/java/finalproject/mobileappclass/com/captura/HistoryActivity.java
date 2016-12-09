package finalproject.mobileappclass.com.captura;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Collections;

import finalproject.mobileappclass.com.captura.Models.TranslationRequest;
import finalproject.mobileappclass.com.captura.DatabaseHelper.CapturaDatabaseHelper;

public class HistoryActivity extends AppCompatActivity{
    private ArrayList<TranslationRequest> requests = new ArrayList<TranslationRequest>();
    private HistoryAdapter adapter;
    private ListView historyOfRequests;
    private boolean studyMode = false;
    private int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        new DBLoadTask().execute();

        historyOfRequests = (ListView) findViewById(R.id.HistoryListView);
        adapter = new HistoryAdapter(getApplicationContext(), requests);
        historyOfRequests.setAdapter(adapter);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_history);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_photo) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                if (tabId == R.id.tab_quizzes) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.

                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    startActivity(intent);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_historyactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:

                Intent intent = new Intent(HistoryActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DBLoadTask extends AsyncTask<Void, Void, ArrayList<TranslationRequest>> {
        ArrayList<TranslationRequest> translationRequests = new ArrayList<TranslationRequest>();

        @Override
        protected ArrayList<TranslationRequest> doInBackground(Void... voids) {
            CapturaDatabaseHelper dbHelper = CapturaDatabaseHelper.getInstance(getApplicationContext());
            translationRequests = dbHelper.getEntireHistory();
            return translationRequests;
        }

        @Override
        protected void onPostExecute(ArrayList<TranslationRequest> dbLocations) {
            requests.clear();
            requests.addAll(dbLocations);
            for(TranslationRequest t : requests)
            {
                Log.v("AndroidCaptura", t.getInputWord() + " " + t.getLanguageName());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
