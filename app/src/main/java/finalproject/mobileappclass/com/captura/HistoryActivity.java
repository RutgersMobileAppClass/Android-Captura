package finalproject.mobileappclass.com.captura;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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
