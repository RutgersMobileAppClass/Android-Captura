package finalproject.mobileappclass.com.captura.Credentials;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Nikhil on 11/27/16.
 */

public class CredentialFetcher
{
    private Properties capturaProperties;
    private Context context;

    public CredentialFetcher(Context context)
    {
        this.context = context;
        capturaProperties = new Properties();
    }

    public Properties loadPropertiesFile(String fileName)
    {
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            capturaProperties.load(inputStream);
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        return capturaProperties;
    }
}
