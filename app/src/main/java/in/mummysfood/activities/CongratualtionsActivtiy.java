package in.mummysfood.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.widgets.CkdTextview;

public class CongratualtionsActivtiy extends BaseActivity {

    @BindView(R.id.gotItAction)
    CkdTextview gotItBUtton;


    @BindView(R.id.visitAction)
    CkdTextview visitAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placed_order_layout);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.visitAction)
    public void setVisitAction()
    {
        Intent ActIntent = new Intent(CongratualtionsActivtiy.this,MainBottomBarActivity.class);
        ActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ActIntent);
        finish();
    }

    @OnClick(R.id.gotItAction)
    public void setgotItAction()
    {
        Intent ActIntent = new Intent(CongratualtionsActivtiy.this,MainBottomBarActivity.class);
        ActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ActIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent ActIntent = new Intent(CongratualtionsActivtiy.this,MainBottomBarActivity.class);
        ActIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ActIntent);
        finish();
    }
}
