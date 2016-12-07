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

        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.setter);
        button.setSize(FloatingActionButton.SIZE_NORMAL);
        button.setColorNormalResId(R.color.colorPrimary);
        button.setColorPressedResId(R.color.colorPrimaryDark);
        button.setIcon(R.drawable.ic_visibility_off);
        button.setStrokeVisible(false);

        //Study Mode Toggle
        findViewById(R.id.setter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!studyMode){
                    studyMode = true;
                    button.setIcon(R.drawable.ic_visibility_on);
                    Toast.makeText(HistoryActivity.this, "Study Mode On!", Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < requests.size(); i++) {
                        TextView hide = (TextView) historyOfRequests.getChildAt(i).findViewById(R.id.ToWordTextView);
                        hide.setVisibility(View.INVISIBLE);
                    }

                    historyOfRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            TextView show = (TextView) historyOfRequests.getChildAt(position).findViewById(R.id.ToWordTextView);
                            int i = show.getVisibility();
                            if(i == 4) {
                                show.setVisibility(View.VISIBLE);
                            }else{
                                show.setVisibility(View.INVISIBLE);
                            }

                        }
                    });

                }else{
                    studyMode = false;
                    button.setIcon(R.drawable.ic_visibility_off);
                    Toast.makeText(HistoryActivity.this, "Study Mode Off!", Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < requests.size(); i++) {
                        TextView hide = (TextView) historyOfRequests.getChildAt(i).findViewById(R.id.ToWordTextView);
                        hide.setVisibility(View.VISIBLE);
                    }
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
            adapter.notifyDataSetChanged();
        }
    }
}
