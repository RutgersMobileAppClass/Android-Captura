package finalproject.mobileappclass.com.captura;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import finalproject.mobileappclass.com.captura.Models.TranslationRequest;

/**
 * Created by viral on 12/5/16.
 */

public class HistoryAdapter extends ArrayAdapter<TranslationRequest> {
    public HistoryAdapter(Context context, ArrayList<TranslationRequest> requests) {
        super(context, 0, requests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TranslationRequest request = getItem(position);
        String language = "";
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.history_item, parent, false);
        }
        String[] Languages = getContext().getResources().getStringArray(R.array.Languages);
        String[] LanguageCodes = getContext().getResources().getStringArray(R.array.Languages_codes);


        if (request != null) {
            TextView inputWord = (TextView) convertView.findViewById(R.id.FromWordTextView);
            TextView translatedWord = (TextView) convertView.findViewById(R.id.ToWordTextView);
            TextView languageOfInterest = (TextView) convertView.findViewById(R.id.ToLanguageTextView);

            for(int i = 0; i < LanguageCodes.length; i++){
                if(request.getLanguageOfInterest().equals(LanguageCodes[i])){
                    language = Languages[i];
                }
            }

            if (inputWord != null) {
                inputWord.setText(request.getInputWord());
            }

            if (translatedWord != null) {
                translatedWord.setText(request.getTranslatedWord());
            }

            if (language != null) {
                languageOfInterest.setText(language);
            }
        }

        return convertView;
    }
}
