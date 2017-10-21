package br.com.mobiclub.quantoprima.ui.adapter.store.survey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.core.service.api.entity.SurveyQuestion;

public class SurveyAdapter extends ArrayAdapter<SurveyQuestion> {

    private List<SurveyQuestion> items;
    private Context context;

    public SurveyAdapter(Context context, List<SurveyQuestion> items) {
        super(context, R.layout.list_item_question, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        View view = convertView;
        SurveyQuestion itemList = items.get(index);
        final int position = index;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_question, null);
        }
        if (itemList != null) {
            TextView txt = (TextView) view.findViewById(R.id.lblQuestionRow);
            txt.setText(itemList.getQuestion());

            RatingBar rb = (RatingBar) view.findViewById(R.id.rbQuestionRow);
            rb.setRating(itemList.getNota());
            rb.setStepSize(1);
            rb.setNumStars(5);
            rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    changeRattingScore(ratingBar, rating, position, fromUser);
                }
            });

        }
        return view;
    }

    public void changeRattingScore(RatingBar ratingBar, float rating, int position, boolean fromUser) {
        if (fromUser) {
            this.items.get(position).setNota((int) rating);
            ratingBar.setRating(rating);
        }
    }
}