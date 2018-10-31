package in.mummysfood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mummysfood.base.BaseActivity;
import in.mummysfood.models.DashBoardModel;
import in.mummysfood.models.ProfileModel;
import in.mummysfood.widgets.CkdTextview;

public class ManageAddressesActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<ProfileModel.Addresses> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_addresses);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.manage_address));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() != null) {

            try {
                addresses = (ArrayList<ProfileModel.Addresses>) getIntent().getSerializableExtra("addresses");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
