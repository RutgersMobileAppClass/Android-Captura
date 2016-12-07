package finalproject.mobileappclass.com.captura;

import android.content.Context;
import android.content.Entity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.ArrayList;

import finalproject.mobileappclass.com.captura.Models.TagTranslation;

/**
 * Created by viral on 12/6/16.
 */

public class GeneratedTagAdapter extends ArrayAdapter<TagTranslation> {
    public GeneratedTagAdapter(Context context, ArrayList<TagTranslation> tags) {
        super(context, 0, tags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TagTranslation tags = getItem(position);

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

            if(translatedTag != null) {
                translatedTag.setText(tags.getTranslatedTag());
            }
        }

        return convertView;
    }
}
