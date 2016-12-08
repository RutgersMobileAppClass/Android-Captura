package finalproject.mobileappclass.com.captura;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;
import android.util.Log;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import finalproject.mobileappclass.com.captura.DatabaseHelper.CapturaDatabaseHelper;

import finalproject.mobileappclass.com.captura.ImageHandling.RealPathUtil;
import finalproject.mobileappclass.com.captura.Models.QuizScore;
import finalproject.mobileappclass.com.captura.Models.TranslationRequest;
import finalproject.mobileappclass.com.captura.SharedPreferencesHelper.PrefSingleton;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    PrefSingleton prefSingleton;
    private boolean permissionsGranted = false;
    private boolean hasTakenOrSelectedPhoto = false;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int IMG_CAPTURE_REQUEST_CODE = 200;
    private static final int IMG_UPLOAD_REQUEST_CODE = 300;
    private TextToSpeech textToSpeech;
    private String translatedWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();

        prefSingleton = PrefSingleton.getInstance();
        prefSingleton.init(getApplicationContext());

        if(PrefSingleton.getInstance().readPreference("language_code") == null){

            PrefSingleton.getInstance().writePreference("language_code", "en");
            PrefSingleton.getInstance().writePreference("language_name", "English");
        }
        textToSpeech = new TextToSpeech(getApplicationContext(), this);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_photo);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_history) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
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


        final FloatingActionButton fabUpload = (FloatingActionButton) findViewById(R.id.fab_upload);
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMG_UPLOAD_REQUEST_CODE);
            }
        });

        final FloatingActionButton fabPicture = (FloatingActionButton) findViewById(R.id.fab_picture);
        fabPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permissionsGranted) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, IMG_CAPTURE_REQUEST_CODE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Need Permission To Use Camera", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Test DB FAB
        final FloatingActionButton fabDBTest = (FloatingActionButton) findViewById(R.id.fab_db_test);
        fabDBTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CapturaDatabaseHelper capturaDatabaseHelper = CapturaDatabaseHelper.getInstance(getApplicationContext());
                TranslationRequest translationRequest = new TranslationRequest("Hello", "Bonjour", "fr");
                TranslationRequest translationRequest1 = new TranslationRequest("Hello", "Hola", "es");
                TranslationRequest translationRequest2 = new TranslationRequest("Dog", "Canis", "la");
                TranslationRequest translationRequest3 = new TranslationRequest("Elephant", "Elefante", "pt");
                TranslationRequest translationRequest4 = new TranslationRequest("Bye", "Doei", "nl");
                TranslationRequest translationRequest5 = new TranslationRequest("Apple", "Sagar", "eu");
                QuizScore quizScore = new QuizScore(10, "Test timestamp", "fr");
                QuizScore quizScore1 = new QuizScore(10, "Test timestamp2", "esp");

                //insert
                capturaDatabaseHelper.insertTranslationRequest(translationRequest);
                capturaDatabaseHelper.insertTranslationRequest(translationRequest);
                capturaDatabaseHelper.insertTranslationRequest(translationRequest1);
                capturaDatabaseHelper.insertTranslationRequest(translationRequest1);
                capturaDatabaseHelper.insertTranslationRequest(translationRequest2);
                capturaDatabaseHelper.insertTranslationRequest(translationRequest3);
                capturaDatabaseHelper.insertTranslationRequest(translationRequest4);
                capturaDatabaseHelper.insertTranslationRequest(translationRequest5);
                capturaDatabaseHelper.insertQuizScore(quizScore);
                capturaDatabaseHelper.insertQuizScore(quizScore1);

                //query
                ArrayList<TranslationRequest> requestList = capturaDatabaseHelper.getEntireHistory();
                ArrayList<QuizScore> quizScoreArrayList = capturaDatabaseHelper.getAllScores();

                for(TranslationRequest tr : requestList) {
                    Log.v("AndroidCaptura", tr.getInputWord() + " " + tr.getTranslatedWord() + " " + tr.getLanguageOfInterest());
                }

                for(QuizScore qs : quizScoreArrayList) {
                    Log.v("AndroidCaptura", ""+qs.getQuizScore() + " " + qs.getTimeStamp() + " " + qs.getLanguageOfInterest());
                }

                ArrayList<TranslationRequest> requestArrayList = capturaDatabaseHelper.findTranslationRequestsByLanguage("fr");
                ArrayList<QuizScore> quizScores = capturaDatabaseHelper.findQuizScoresByLanguage("es");

                for(TranslationRequest t : requestArrayList) {
                    Log.v("AndroidCaptura", t.getInputWord() + " " + t.getTranslatedWord() + " " + t.getLanguageOfInterest());
                }

                for(QuizScore q : quizScores) {
                    Log.v("AndroidCaptura", ""+q.getQuizScore() + " " + q.getLanguageOfInterest() + " " + q.getTimeStamp());
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:

                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkAndRequestPermissions() {
        int cameraResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int readExtResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExtResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if ((cameraResult != PackageManager.PERMISSION_GRANTED) || (readExtResult != PackageManager.PERMISSION_GRANTED) || (writeExtResult != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            permissionsGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Captura Permissions are granted!", Toast.LENGTH_SHORT).show();
                permissionsGranted = true;
            } else {
                Toast.makeText(getApplicationContext(), "Need to enable Permissions!", Toast.LENGTH_LONG).show();
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    //TODO: EDIT THIS AS NEEDED TO HANDLE IMAGE CAPTURE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(getApplicationContext(), DisplayResultsActivity.class);


        if (requestCode == IMG_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            intent.putExtra("image", imageBitmap);
            intent.putExtra("request", IMG_CAPTURE_REQUEST_CODE);
            startActivity(intent);
            hasTakenOrSelectedPhoto = true;
        } else if (requestCode == IMG_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            try {
                String filepath = RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), imageUri);
                intent.putExtra("request", IMG_UPLOAD_REQUEST_CODE);
                intent.putExtra("imageFilepath", filepath);
                startActivity(intent);
                hasTakenOrSelectedPhoto = true;
            } catch (Exception e) {
                Log.e("Captura", e.getMessage());
            }
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            setTextToSpeechLanguage();
        }
    }

    public void setTextToSpeechLanguage() {
        if(textToSpeech.isLanguageAvailable(new Locale(prefSingleton.readPreference("language_code"))) == TextToSpeech.LANG_AVAILABLE) {
            Toast.makeText(getApplicationContext(), "TTS supported", Toast.LENGTH_SHORT).show();
            textToSpeech.setLanguage(new Locale(prefSingleton.readPreference("language_code")));
        }
        else {
            Toast.makeText(getApplicationContext(), "TTS not supported", Toast.LENGTH_SHORT).show();
            textToSpeech.setLanguage(Locale.US);
        }
    }
}