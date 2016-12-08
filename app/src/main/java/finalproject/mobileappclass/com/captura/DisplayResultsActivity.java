package finalproject.mobileappclass.com.captura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.ArrayList;
import java.util.List;

import finalproject.mobileappclass.com.captura.DatabaseHelper.CapturaDatabaseHelper;
import finalproject.mobileappclass.com.captura.ImageHandling.CloudVisionWrapper;
import finalproject.mobileappclass.com.captura.ImageHandling.ExifUtil;
import finalproject.mobileappclass.com.captura.Models.TagTranslation;
import finalproject.mobileappclass.com.captura.Models.TranslationRequest;
import finalproject.mobileappclass.com.captura.SharedPreferencesHelper.PrefSingleton;
import finalproject.mobileappclass.com.captura.Translation.GoogleTranslateWrapper;

/**
 * Created by viral on 12/6/16.
 */

public class DisplayResultsActivity extends AppCompatActivity {
    private static final int IMG_CAPTURE_REQUEST_CODE = 200;
    private static final int IMG_UPLOAD_REQUEST_CODE = 300;

    private ArrayList<EntityAnnotation> old = new ArrayList<EntityAnnotation>();
    private ArrayList<TagTranslation> tags = new ArrayList<TagTranslation>();
    private ArrayList<String> keywords = new ArrayList<String>();
    private ArrayList<String> translations = new ArrayList<String>();
    private GeneratedTagAdapter adapter;
    private ListView listOfTags;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        listOfTags = (ListView) findViewById(R.id.GeneratedTagsListView);
        adapter = new GeneratedTagAdapter(this, tags);
        listOfTags.setAdapter(adapter);

        Intent intent = getIntent();
        ImageView imageView = (ImageView) findViewById(R.id.object_photo);

        int request = intent.getIntExtra("request", 0);
        if(request == IMG_CAPTURE_REQUEST_CODE) {
            Bitmap bitmap = intent.getParcelableExtra("image");
            Bitmap image = getResizedBitmap(bitmap, 1100, 1100);
            imageView.setImageBitmap(image);
        } else if(request == IMG_UPLOAD_REQUEST_CODE) {
            Bundle extras = intent.getExtras();
            String filepath = (String) extras.get("imageFilepath");
            Bitmap image = decodeFile(filepath);
            imageView.setImageBitmap(image);
        }

        new ImageRecognitionTask().execute(((BitmapDrawable)imageView.getDrawable()).getBitmap());

        listOfTags.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v, int position, long id) {
                try {
                    CapturaDatabaseHelper capturaDatabaseHelper = CapturaDatabaseHelper.getInstance(getApplicationContext());
                    TranslationRequest translationRequest = new TranslationRequest(tags.get(position).getTag(),
                            tags.get(position).getTranslatedTag(), PrefSingleton.getInstance().readPreference("language_code"));
                    capturaDatabaseHelper.insertTranslationRequest(translationRequest);

                    Log.v("AndroidCaptura", "Inserted translation request...");
                    return true;
                } catch (Exception e) {
                    Log.v("AndroidCaptura", e.toString());
                    return false;
                }
            }
        });
    }

    public Bitmap decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        Bitmap b = ExifUtil.rotateBitmap(filePath, b1);
        return b;
    }

    //Referenced from: http://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private class ImageRecognitionTask extends AsyncTask<Bitmap, Void, List<EntityAnnotation>> {
        @Override
        protected List<EntityAnnotation> doInBackground(Bitmap... bitmaps) {
            CloudVisionWrapper cloudVisionWrapper = new CloudVisionWrapper(bitmaps[0], getApplicationContext());
            return cloudVisionWrapper.performImageRecognition();
        }

        @Override
        protected void onPostExecute(List<EntityAnnotation> s) {
            for(EntityAnnotation label : s) {
                keywords.add(label.getDescription());
                new GoogleTranslateTask().execute(label.getDescription(), "en", PrefSingleton.getInstance().readPreference("language_code"));
            }
        }
    }

    private class  GoogleTranslateTask extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            GoogleTranslateWrapper googleTranslateWrapper = new GoogleTranslateWrapper(getApplicationContext());
            translations.add(googleTranslateWrapper.translate(strings[0], strings[1], strings[2]));
            return counter++;
        }

        @Override
        protected void onPostExecute(Integer counter) {
            tags.add(new TagTranslation(keywords.get(counter).toString(), translations.get(counter).toString()));
            Log.v("Translation: ", tags.get(counter).toString());
            adapter.notifyDataSetChanged();
        }
    }
}