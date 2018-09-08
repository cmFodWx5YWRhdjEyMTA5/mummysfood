package in.mummysfood.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.widget.Toast;


/**
 * Created by bindu on 1/9/17.
 */

public class BaseFragment extends Fragment {



    public ProgressDialog pd;


    public void showProgress(String msg) {
        if (pd != null && pd.isShowing()) {
            dismissProgress();
        } else {
            pd = getProgressDialog(getActivity(), msg);
            pd.show();
        }
    }

    public static ProgressDialog getProgressDialog(Context mCon, String Msg) {

        ProgressDialog pd = new ProgressDialog(mCon);

        if(!BuildConfig.DEBUG){
            pd.setCancelable(false);
        }
        pd.setMessage(Msg);

        return pd;

    }

    public void dismissProgress() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void addFood(){

    }

    public void subFood(){

    }


}
