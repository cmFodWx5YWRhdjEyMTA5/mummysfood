package com.mf.mumizzfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.mf.mumizzfood.R;
import com.mf.mumizzfood.base.BaseActivity;
import com.mf.mumizzfood.adapters.QuestionsAdapter;
import com.mf.mumizzfood.models.QuestionsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RateYourselfActivity extends BaseActivity {

    @BindView(R.id.question_listview)
    RecyclerView recyclerView;
    @BindView(R.id.submit_question_answer)
    Button submit_question_answer;

    LinearLayoutManager linearLayoutManager;
    QuestionsAdapter queAdapter;
    ArrayList<QuestionsModel> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_yourself);
        ButterKnife.bind(this);

        prepareQuestionsData();
        queAdapter = new QuestionsAdapter(this,questionList);
        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(queAdapter);
    }

    @OnClick(R.id.submit_question_answer)
    public void SubmitQuesAns(){
        showToast("submit question answers");
        Intent intent = new Intent(this, MainBottomBarActivity.class);
        startActivity(intent);
    }

    private void prepareQuestionsData() {
        for (int i = 0; i<=5; i++){
            questionList.add(new QuestionsModel("question "+i,"option1","option2","option3"));
        }

    }
}
