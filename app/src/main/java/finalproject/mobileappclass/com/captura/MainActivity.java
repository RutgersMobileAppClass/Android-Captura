package finalproject.mobileappclass.com.captura;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.view.View;


public class MainActivity extends AppCompatActivity {


    private boolean permissionsGranted = false;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int IMG_CAPTURE_REQUEST_CODE = 200;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();


        Button takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        imageView = (ImageView) findViewById(R.id.imageholder);

        //If user wants to take an image from the camera
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permissionsGranted)
                {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                    {
                        startActivityForResult(takePictureIntent, IMG_CAPTURE_REQUEST_CODE);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Need Permission To Use Camera", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void checkAndRequestPermissions()
    {
        int cameraResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int readExtResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExtResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if((cameraResult != PackageManager.PERMISSION_GRANTED) || (readExtResult != PackageManager.PERMISSION_GRANTED) || (writeExtResult != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, new String[] {CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        }
        else
        {
            permissionsGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode == PERMISSIONS_REQUEST_CODE)
        {
            if(grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Captura Permissions are granted!" , Toast.LENGTH_SHORT).show();
                permissionsGranted = true;
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Need to enable Permissions!", Toast.LENGTH_LONG).show();
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMG_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
