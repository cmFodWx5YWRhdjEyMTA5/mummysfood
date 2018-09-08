package in.mummysfood.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;

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