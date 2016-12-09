package finalproject.mobileappclass.com.captura;

/**
 * Created by Nikhil on 12/8/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import finalproject.mobileappclass.com.captura.Models.QuizScore;
import finalproject.mobileappclass.com.captura.Models.TranslationRequest;

/**
 * Created by Neil on 12/7/2016.
 */

public class QuizAdapter extends ArrayAdapter<QuizScore>{
    public QuizAdapter (Context context, ArrayList<QuizScore> scores) {
        super(context, 0, scores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuizScore score = getItem(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.quiz_item, parent, false);
        }

        if (score != null) {
            TextView quizTargetLanguageTextView = (TextView) convertView.findViewById(R.id.quizToLanguageTextView);
            TextView quizDateTextView = (TextView) convertView.findViewById(R.id.quizDateTextView);
            TextView quizScoreTextView = (TextView) convertView.findViewById(R.id.quizScore);

            quizTargetLanguageTextView.setText(score.getLanguageName());
            quizDateTextView.setText(score.getTimeStamp());
            quizScoreTextView.setText(""+score.getQuizScore() + "/" + score.getNumQuestions());
        }

        return convertView;
    }
}
