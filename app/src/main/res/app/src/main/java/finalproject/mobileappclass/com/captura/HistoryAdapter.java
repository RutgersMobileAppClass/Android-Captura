package finalproject.mobileappclass.com.captura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.history_item, parent, false);
        }

        if (request != null) {
            TextView inputWord = (TextView) convertView.findViewById(R.id.FromWordTextView);
            TextView translatedWord = (TextView) convertView.findViewById(R.id.ToWordTextView);
            TextView languageOfInterest = (TextView) convertView.findViewById(R.id.ToLanguageTextView);

            if (inputWord != null) {
                inputWord.setText(request.getInputWord());
            }

            if (translatedWord != null) {
                translatedWord.setText(request.getTranslatedWord());
            }

            if (languageOfInterest != null) {
                languageOfInterest.setText(request.getLanguageOfInterest());
            }
        }

        return convertView;
    }
}
