package finalproject.mobileappclass.com.captura.ImageHandling;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import finalproject.mobileappclass.com.captura.Credentials.CredentialFetcher;

/**
 * Created by Nikhil on 12/3/16.
 */

public class CloudVisionWrapper {
    private String apiKey;
    private Bitmap bitmap;
    private Context context;


    public CloudVisionWrapper(Bitmap bitmap, Context context) {
        this.bitmap = bitmap;
        this.context = context;
    }

    public void init() {
        CredentialFetcher credentialFetcher = new CredentialFetcher(context);
        Properties properties = credentialFetcher.loadPropertiesFile("captura.properties");
        String encodedKey = properties.getProperty("googleapikey");
        apiKey = new String(Base64.decode(encodedKey.getBytes(), Base64.DEFAULT));
    }

    public List<EntityAnnotation> performImageRecognition() {
        try {
            init();
            HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
            builder.setVisionRequestInitializer(new
                    VisionRequestInitializer(apiKey));
            Vision vision = builder.build();

            BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                    new BatchAnnotateImagesRequest();
            batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();


                Image base64EncodedImage = new Image();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                base64EncodedImage.encodeContent(imageBytes);
                annotateImageRequest.setImage(base64EncodedImage);

                annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                    Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(10);
                    add(labelDetection);
                }});

                add(annotateImageRequest);
            }});


            Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
            annotateRequest.setDisableGZipContent(true);
            BatchAnnotateImagesResponse response = annotateRequest.execute();
            return convertResponseToString(response);
        }
        catch(Exception e) {
            Log.e("AndroidCaptura Ex", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<EntityAnnotation> convertResponseToString(BatchAnnotateImagesResponse response) {
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        return labels;
    }
}
