package in.mummysfood.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import in.mummysfood.R;
import in.mummysfood.base.BaseActivity;

/**
 * Created by acer on 4/15/2018.
 */

public class SpashAcctivity extends BaseActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.splashscreen);
    }
}
