package finalproject.mobileappclass.com.captura.Translation;

import android.content.Context;

import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import finalproject.mobileappclass.com.captura.Credentials.CredentialFetcher;

import static android.R.id.input;


/**
 * Created by Nikhil on 11/27/16.
 */

public class GoogleTranslateWrapper
{
    private Context context;
    private String apiKey;

    public GoogleTranslateWrapper(Context context)
    {
        this.context = context;
    }

    public void init()
    {
        CredentialFetcher credentialFetcher = new CredentialFetcher(context);
        Properties properties = credentialFetcher.loadPropertiesFile("captura.properties");
        String encodedKey = properties.getProperty("googleapikey");
        apiKey = new String(Base64.decode(encodedKey.getBytes(), Base64.DEFAULT));
    }

    public String translate(String inputText, String inputLanguage, String outputLanguage)
    {
        StringBuilder response = new StringBuilder();
        try
        {
            init();
            String encodedInputText = URLEncoder.encode(inputText, "UTF-8");
            String requestUrl = "https://www.googleapis.com/language/translate/v2?key=" + apiKey +
                    "&q=" + encodedInputText + "&target=" + outputLanguage + "&source=" + inputLanguage;
            Log.v("AndroidCaptura", requestUrl);
            URL url = new URL(requestUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

            InputStream inputStream;
            if(httpsURLConnection.getResponseCode() == 200)
            {
                inputStream = httpsURLConnection.getInputStream();
            }
            else
            {
                inputStream = httpsURLConnection.getErrorStream();
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                response.append(line);
            }

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(response.toString());

            if(jsonElement.isJsonObject())
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if(jsonObject.get("error") == null)
                {

                    String outputText = jsonObject.get("data").getAsJsonObject().get("translations").getAsJsonArray()
                            .get(0).getAsJsonObject().get("translatedText").getAsString();
                    return outputText;
                }
            }

            if(httpsURLConnection.getResponseCode() != 200)
            {
                Log.e("AndroidCaptura", response.toString());
                return response.toString();
            }
        }
        catch(Exception e)
        {
            Log.e("AndroidCaptura", e.getMessage());
        }
        return null;
    }
}
