/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package in.ckd.calenderkhanado.utils;

import in.ckd.calenderkhanado.data.network.RetrofitApiService;

/**
 * Created by Niel on 19/07/17.
 */

public final class AppConstants {

    public static final String BASE_URL = "mummysfood.in/";
    public static final String ONESINGAL = "f313ca24-ca6e-45c0-b7ec-e668861b51c7";

    public static final String STATUS_CODE_SUCCESS = "success";
    public static final String STATUS_CODE_FAILED = "failed";
    public static final String STATUS_CODE_ALREADY = "already";

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final String USERCREATE = "UserCreated";
    public static final String USERCREATEVALUE = "done";

    public static final long NULL_INDEX = -1L;

    public static final String SEED_DATABASE_OPTIONS = "seed/options.json";
    public static final String SEED_DATABASE_QUESTIONS = "seed/questions.json";

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";

    public static RetrofitApiService restAPI = RetrofitApiService.retrofit.create(RetrofitApiService.class);

    public static final String LocationSaved = "Saved";
    public static final String LocationSavedValue = "SavedLocation";

    public static String  MANUALOTP= "251601";
    public static String  SUCCESS= "success";
    public static String  ALREADY= "already";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static final String CHEF = "chef";
    public static final String SEEKER = "seeker";
    public static final String GOOGLE = "google";
    public static final String MOBILE = "mobile";

    public static final String IMAGEURL = "http://cdn.mummysfood.in/";
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 247;

    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
