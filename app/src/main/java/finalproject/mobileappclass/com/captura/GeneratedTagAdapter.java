package finalproject.mobileappclass.com.captura;

import android.content.Context;
import android.content.Entity;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.ArrayList;
import java.util.Locale;

import finalproject.mobileappclass.com.captura.Models.TagTranslation;
import finalproject.mobileappclass.com.captura.SharedPreferencesHelper.PrefSingleton;

/**
 * Created by viral on 12/6/16.
 */

public class GeneratedTagAdapter extends ArrayAdapter<TagTranslation> implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;

    public GeneratedTagAdapter(Context context, ArrayList<TagTranslation> tags) {
        super(context, 0, tags);
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TagTranslation tags = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.tag_item, parent, false);
        }

        if (tags != null) {
            TextView generatedTag = (TextView) convertView.findViewById(R.id.EnglishTagTextView);
            TextView translatedTag = (TextView) convertView.findViewById(R.id.TranslatedTagTextView);

            if (generatedTag != null) {
                generatedTag.setText(tags.getTag());
            }

            if (translatedTag != null) {
                translatedTag.setText(tags.getTranslatedTag());
            }

            Button pronunciationButton = (Button) convertView.findViewById(R.id.pronunciationButton);
            pronunciationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String translatedWord = tags.getTranslatedTag();
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
        String languageCode = PrefSingleton.getInstance().readPreference("language_code");
        if(textToSpeech.isLanguageAvailable(new Locale(languageCode)) == TextToSpeech.LANG_AVAILABLE) {
            textToSpeech.setLanguage(new Locale(languageCode));
        }
        else {
            Toast.makeText(getContext(), "This language does not have TTS support", Toast.LENGTH_LONG).show();
            textToSpeech.setLanguage(Locale.US);
        }
    }
}
