package finalproject.mobileappclass.com.captura.Credentials;

/**
 * Created by Neil on 11/28/2016.
 * Custom class used to wrap data passed to Clarifai within the ImageDetectionTask AsyncTask.
 */

public class ImageDetectionCredentialsWrapper {
    private String clientId;
    private String clientSecret;
    private byte[] imageByteArray;

    public ImageDetectionCredentialsWrapper(String clientId, String clientSecret, byte[] imageByteArray) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.imageByteArray = imageByteArray;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }
}
