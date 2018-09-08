package in.mummysfood.activities;

import android.content.Intent;
import android.os.Bundle;

import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.widgets.CkdTextview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InterestActivity  extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.interest_next)
    public void nextBtnClick(){
        showToast("next click");
        Intent intent = new Intent(this,ChooseFoodTypeActivity.class);
        startActivity(intent);
    }

}
