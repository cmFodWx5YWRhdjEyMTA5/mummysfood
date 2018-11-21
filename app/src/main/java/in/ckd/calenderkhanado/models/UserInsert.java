package in.ckd.calenderkhanado.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer on 9/30/2018.
 */

public class UserInsert {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("f_name")
        public String f_name;
        @SerializedName("l_name")
        public String l_name;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("email")
        public String email;
        @SerializedName("is_mobile_verified")
        public String is_mobile_verified;
        @SerializedName("is_email_verified")
        public String is_email_verified;
        @SerializedName("type")
        public String type;
        @SerializedName("profile_image")
        public String profile_image;
        @SerializedName("is_vagitarian")
        public String is_vagitarian;
        @SerializedName("os")
        public String os;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("id")
        public int id;
    }
}
