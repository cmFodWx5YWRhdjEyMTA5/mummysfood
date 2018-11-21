package in.ckd.calenderkhanado.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ckd.calenderkhanado.R;
import in.ckd.calenderkhanado.base.BaseActivity;

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