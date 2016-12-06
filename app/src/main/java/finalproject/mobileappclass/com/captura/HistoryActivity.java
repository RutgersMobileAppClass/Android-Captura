package finalproject.mobileappclass.com.captura;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

import finalproject.mobileappclass.com.captura.Models.TranslationRequest;
import finalproject.mobileappclass.com.captura.DatabaseHelper.CapturaDatabaseHelper;

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<TranslationRequest> requests = new ArrayList<TranslationRequest>();
    private HistoryAdapter adapter;
    private ListView historyOfRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        new DBLoadTask().execute();

        historyOfRequests = (ListView) findViewById(R.id.HistoryListView);
        adapter = new HistoryAdapter(this, requests);
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

                Toast.makeText(HistoryActivity.this, "Settings!", Toast.LENGTH_SHORT).show();


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
            adapter.notifyDataSetChanged();
        }
    }
}
