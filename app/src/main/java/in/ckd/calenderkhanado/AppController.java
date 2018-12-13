package in.ckd.calenderkhanado;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;


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

    // OneSignal Initialization
    OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();

    Fresco.initialize(this);

    //AppController.context = getApplicationContext();
    //Firebase.setAndroidContext(this);

  }


}
