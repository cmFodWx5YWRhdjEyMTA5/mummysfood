package com.mf.mumizzfood.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mf.mumizzfood.R;
import com.mf.mumizzfood.base.BaseActivity;

public class ChooseFoodTypeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_food_type);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.choose_food_type_)
    public void nextBtnClick(){
        showToast("next click");
        Intent intent = new Intent(this,RateYourselfActivity.class);
        startActivity(intent);
    }

}