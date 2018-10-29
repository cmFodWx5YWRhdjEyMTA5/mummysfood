package in.mummysfood.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mummysfood.R;
import in.mummysfood.adapters.ViewPagerForHomeSlider;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.widgets.CirclePageIndicator;
import in.mummysfood.widgets.CkdTextview;
import me.relex.circleindicator.CircleIndicator;

public class CongratualtionsActivtiy extends BaseActivity {

    @BindView(R.id.gotItAction)
    CkdTextview gotItBUtton;


    @BindView(R.id.visitAction)
    CkdTextview visitAction;

    @BindView(R.id.FullScreenViewPgaer)
    ViewPager FullScreenViewPgaer;

    @BindView(R.id.circleIndicator)
    CirclePageIndicator  circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placed_order_layout);
        ButterKnife.bind(this);

        List<String>foodList = new ArrayList<>();
        foodList.add("All you need is love. But a little chocolate now and then doesn't hurt.");
        foodList.add("Pull up a chair. Take a taste. Come join us. Life is so endlessly delicious.");
        foodList.add("There is no love sincerer than the love of food.");

        ViewPagerForHomeSlider gallaryAdapter = new ViewPagerForHomeSlider(this,foodList);
        FullScreenViewPgaer.setAdapter(gallaryAdapter);

        circleIndicator.setViewPager(FullScreenViewPgaer);
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
