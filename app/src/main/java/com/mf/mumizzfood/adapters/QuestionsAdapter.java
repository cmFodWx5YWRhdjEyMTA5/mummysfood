package com.mf.mumizzfood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mf.mumizzfood.R;
import com.mf.mumizzfood.models.QuestionsModel;
import com.mf.mumizzfood.widgets.CkdTextview;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<QuestionsModel> questionList;

    public QuestionsAdapter(Context context, ArrayList<QuestionsModel> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CkdTextview quetion;
        RadioButton option1;
        RadioButton option2;
        RadioButton option3;


        public ViewHolder(View itemView) {
            super(itemView);

            quetion = (CkdTextview) itemView.findViewById(R.id.question);
            option1 = (RadioButton) itemView.findViewById(R.id.radioButton1);
            option2 = (RadioButton) itemView.findViewById(R.id.radioButton2);
            option3 = (RadioButton) itemView.findViewById(R.id.radioButton3);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_answers_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return questionList.size()!=0 ? questionList.size():0;
    }
}
