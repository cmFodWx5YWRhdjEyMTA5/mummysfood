package in.ckd.calenderkhanado;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by Niel on 14/07/17.
 */

public class AppController extends Application {
  static Context context;

  @Override public void onCreate() {
    super.onCreate();
    FirebaseApp.initializeApp(this);
    FirebaseInstanceId.getInstance().getToken();

    Stetho.initializeWithDefaults(this);

    //AppController.context = getApplicationContext();
    //Firebase.setAndroidContext(this);

  }


}
