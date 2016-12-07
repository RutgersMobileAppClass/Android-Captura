package finalproject.mobileappclass.com.captura;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

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
                Toast.makeText(QuizActivity.this, "Clicked FAB", Toast.LENGTH_SHORT).show();
            }
        });

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

            case R.id.action_analytics:

                Toast.makeText(QuizActivity.this, "Analytics!", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
