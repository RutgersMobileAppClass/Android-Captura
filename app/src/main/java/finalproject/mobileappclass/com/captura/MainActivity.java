package finalproject.mobileappclass.com.captura;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.view.View;


import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Properties;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import finalproject.mobileappclass.com.captura.Credentials.CredentialFetcher;
import finalproject.mobileappclass.com.captura.Credentials.ImageDetectionCredentialsWrapper;
import finalproject.mobileappclass.com.captura.ImageHandling.ExifUtil;
import finalproject.mobileappclass.com.captura.ImageHandling.RealPathUtil;
import finalproject.mobileappclass.com.captura.Translation.GoogleTranslateWrapper;

public class MainActivity extends AppCompatActivity {


    private boolean permissionsGranted = false;
    private boolean hasTakenOrSelectedPhoto = false;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int IMG_CAPTURE_REQUEST_CODE = 200;
    private static final int IMG_UPLOAD_REQUEST_CODE = 300;
    private ImageView imageView;
    private ListView conceptsListView;
    private ArrayAdapter<String> conceptsListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();

        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        imageView = (ImageView) findViewById(R.id.imageholder);
        Button uploadPhotoButton = (Button) findViewById(R.id.choosePhotoButton);
        Button recognizeImageButton = (Button) findViewById(R.id.recognize_image_button);

        //Set adapter to the list view for Clarifai
        conceptsListView = (ListView) findViewById(R.id.conceptsListView);
        conceptsListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        conceptsListView.setAdapter(conceptsListViewAdapter);

        //If user wants to take an image from the camera
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
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

        //If user wants to upload an existing image
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMG_UPLOAD_REQUEST_CODE);
            }
        });

        recognizeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasTakenOrSelectedPhoto) {
                    //Get credentials for Clarifai API
                    CredentialFetcher credentialFetcher = new CredentialFetcher(getApplicationContext());
                    Properties properties = credentialFetcher.loadPropertiesFile("captura.properties");
                    String encodedClientId = properties.getProperty("clientid");
                    String encodedClientSecret = properties.getProperty("clientsecret");
                    String clientId = new String(Base64.decode(encodedClientId.getBytes(), Base64.DEFAULT));
                    String clientSecret = new String(Base64.decode(encodedClientSecret.getBytes(), Base64.DEFAULT));

                    //Get image view's image as a byte array for usage by the Clarifai API
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap imageBitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    //Wrap into ImageDetectionCredentialsWrapper object
                    ImageDetectionCredentialsWrapper imageDetectionWrapper = new ImageDetectionCredentialsWrapper(clientId, clientSecret, byteArray);
                    new ImageDetectionTask().execute(imageDetectionWrapper);
                }
                else {
                    Toast.makeText(MainActivity.this, "Photo not taken or selected yet!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //translation testing
        final EditText inputText = (EditText) findViewById(R.id.input_field) ;
        final EditText inputCode = (EditText) findViewById(R.id.inputlang_code);
        final EditText outputCode = (EditText) findViewById(R.id.outputlang_code);
        Button translateButton = (Button) findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new GoogleTranslateTask().execute(inputText.getText().toString(), inputCode.getText().toString(), outputCode.getText().toString());
            }
        });
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
        if (requestCode == IMG_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            hasTakenOrSelectedPhoto = true;
        } else if (requestCode == IMG_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            try
            {
                decodeFile(RealPathUtil.getRealPathFromURI_API19(getApplicationContext(), imageUri));
                hasTakenOrSelectedPhoto = true;
            } catch (Exception e) {
                Log.e("Captura", e.getMessage());
            }
        }
    }

    //TODO: May or may not have to create an asynctask for this if the operation causes the UI thread to crash
    public void decodeFile(String filePath) {

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
        imageView.setImageBitmap(b);

    }

    //TODO: Check if async execution of Clarifai predict() API can replace this asynctask
    private class ImageDetectionTask extends AsyncTask<ImageDetectionCredentialsWrapper, Void, List<ClarifaiOutput<Concept>>> {
        @Override
        protected void onPreExecute() {
            conceptsListViewAdapter.clear();
        }

        @Override
        protected List<ClarifaiOutput<Concept>> doInBackground(ImageDetectionCredentialsWrapper... wrappers) {
            String clientId = wrappers[0].getClientId();
            String clientSecret = wrappers[0].getClientSecret();
            byte[] byteArray = wrappers[0].getImageByteArray();

            final ClarifaiClient client = new ClarifaiBuilder(clientId, clientSecret)
                    //TODO: Research the below line to see if this applies for our app
                    // .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                    .buildSync(); // or use .build() to get a Future<ClarifaiClient>
            final List<ClarifaiOutput<Concept>> predictionResults = client.getDefaultModels().generalModel() // You can also do client.getModelByID("id") to get custom models
                    .predict()
                    .withInputs(
                            ClarifaiInput.forImage(ClarifaiImage.of(byteArray))
                    )
                    .executeSync()
                    .get();

            return predictionResults;
        }

        @Override
        protected void onPostExecute(List<ClarifaiOutput<Concept>> list) {
            /*int counter = 1;*/
            for (ClarifaiOutput<Concept> concept: list) {
                for (Concept c: concept.data()) {
                    conceptsListViewAdapter.add(c.name() + ", " + c.value()/* + "\t" + counter*/);
                }
                /*counter++;*/
            }
            Toast.makeText(getApplicationContext(), "Completed Clarifai image detection!", Toast.LENGTH_LONG).show();
        }
    }


    private class  GoogleTranslateTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            GoogleTranslateWrapper googleTranslateWrapper = new GoogleTranslateWrapper(getApplicationContext());
            return googleTranslateWrapper.translate(strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(String s)
        {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    /*
    private class  GoogleTranslateTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids)
        {
            GoogleTranslateWrapper googleTranslateWrapper = new GoogleTranslateWrapper(getApplicationContext());
            return googleTranslateWrapper.translate("Hello", "en", "fr");
        }

        @Override
        protected void onPostExecute(String s)
        {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
    */
}