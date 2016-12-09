package finalproject.mobileappclass.com.captura;

import android.content.Context;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import finalproject.mobileappclass.com.captura.Models.TranslationRequest;
import finalproject.mobileappclass.com.captura.SharedPreferencesHelper.PrefSingleton;

/**
 * Created by viral on 12/5/16.
 */

public class HistoryAdapter extends ArrayAdapter<TranslationRequest> implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private String languageCode = "";

    public HistoryAdapter(Context context, ArrayList<TranslationRequest> requests) {
        super(context, 0, requests);
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TranslationRequest request = getItem(position);
        String language = "";
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.history_item, parent, false);
        }
        String[] Languages = getContext().getResources().getStringArray(R.array.Languages);
        final String[] LanguageCodes = getContext().getResources().getStringArray(R.array.Languages_codes);

        if (request != null) {
            TextView inputWord = (TextView) convertView.findViewById(R.id.FromWordTextView);
            TextView translatedWord = (TextView) convertView.findViewById(R.id.ToWordTextView);
            TextView languageOfInterest = (TextView) convertView.findViewById(R.id.ToLanguageTextView);
            language = request.getLanguageName();
            /*
            for(int i = 0; i < LanguageCodes.length; i++){
                if(request.getLanguageOfInterest().equals(LanguageCodes[i])){
                    language = Languages[i];
                }
            }
            */

            if (inputWord != null) {
                inputWord.setText(request.getInputWord());
            }

            if (translatedWord != null) {
                translatedWord.setText(request.getTranslatedWord());
            }

            if (language != null) {
                languageOfInterest.setText(language);
            }

            Button pronunciationButton = (Button) convertView.findViewById(R.id.pronunciationButton);
            pronunciationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String translatedWord = request.getTranslatedWord();
                    languageCode = request.getLanguageCode();
                    setTextToSpeechLanguage();

                    if (translatedWord != null) {
                        textToSpeech.speak(translatedWord, TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        Toast.makeText(getContext(), "You have not translated a word", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return convertView;
    }

    @Override
    public void onInit(int status)
    {
        if(status == TextToSpeech.SUCCESS) {
            setTextToSpeechLanguage();
        }
    }

    public void setTextToSpeechLanguage()
    {
        if(textToSpeech.isLanguageAvailable(new Locale(languageCode)) == TextToSpeech.LANG_AVAILABLE)
        {
            textToSpeech.setLanguage(new Locale(languageCode));
        }
        else
        {
            textToSpeech.setLanguage(Locale.US);
        }
    }
}
