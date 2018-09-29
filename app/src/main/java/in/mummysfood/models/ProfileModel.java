package in.mummysfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProfileModel implements Serializable {

    @SerializedName("data")
    public List<Data> data;
    @SerializedName("status")
    public String status;

    public static class Data {
        @SerializedName("chef_open_days")
        public Chef_open_days chef_open_days;
        @SerializedName("subscribes")
        public List<String> subscribes;
        @SerializedName("chef_detail")
        public Chef_detail chef_detail;
        @SerializedName("food_detail")
        public Food_detail food_detail;
        @SerializedName("addresses")
        public List<Addresses> addresses;
        @SerializedName("name")
        public String name;
        @SerializedName("login_type")
        public String login_type;
        @SerializedName("gender")
        public String gender;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("is_vagitarian")
        public int is_vagitarian;
        @SerializedName("profile_image")
        public String profile_image;
        @SerializedName("device_id")
        public String device_id;
        @SerializedName("os")
        public String os;
        @SerializedName("type")
        public String type;
        @SerializedName("is_email_verified")
        public int is_email_verified;
        @SerializedName("is_mobile_verified")
        public int is_mobile_verified;
        @SerializedName("email")
        public String email;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("l_name")
        public String l_name;
        @SerializedName("f_name")
        public String f_name;
        @SerializedName("id")
        public int id;
    }

    public static class Chef_open_days {
        @SerializedName("sun_close_hr")
        public String sun_close_hr;
        @SerializedName("sun_open_hr")
        public String sun_open_hr;
        @SerializedName("sat_close_hr")
        public String sat_close_hr;
        @SerializedName("sat_open_hr")
        public String sat_open_hr;
        @SerializedName("fri_close_hr")
        public String fri_close_hr;
        @SerializedName("fri_open_hr")
        public String fri_open_hr;
        @SerializedName("thus_close_hr")
        public String thus_close_hr;
        @SerializedName("thus_open_hr")
        public String thus_open_hr;
        @SerializedName("wed_close_hr")
        public String wed_close_hr;
        @SerializedName("wed_open_hr")
        public String wed_open_hr;
        @SerializedName("tues_close_hr")
        public String tues_close_hr;
        @SerializedName("tues_open_hr")
        public String tues_open_hr;
        @SerializedName("mon_close_hr")
        public String mon_close_hr;
        @SerializedName("mon_open_hr")
        public String mon_open_hr;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("id")
        public int id;
    }

    public static class Chef_detail {
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("about")
        public String about;
        @SerializedName("is_kitchen_varified")
        public int is_kitchen_varified;
        @SerializedName("want_to_learn")
        public int want_to_learn;
        @SerializedName("cooking_score")
        public int cooking_score;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("id")
        public int id;
    }

    public static class Food_detail {
        @SerializedName("food_media")
        public List<Food_media> food_media;
        @SerializedName("price")
        public String price;
        @SerializedName("media_type")
        public String media_type;
        @SerializedName("details")
        public String details;
        @SerializedName("name")
        public String name;
        @SerializedName("category_id")
        public int category_id;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("id")
        public int id;
    }

    public static class Food_media {
        @SerializedName("media")
        public Media media;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("entity_media_type")
        public String entity_media_type;
        @SerializedName("entity_media_id")
        public int entity_media_id;
        @SerializedName("media_id")
        public int media_id;
        @SerializedName("id")
        public int id;
    }

    public static class Media {
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("source")
        public String source;
        @SerializedName("type")
        public String type;
        @SerializedName("name")
        public String name;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("id")
        public int id;
    }

    public static class Addresses {
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("nearby")
        public String nearby;
        @SerializedName("status")
        public String status;
        @SerializedName("pin")
        public String pin;
        @SerializedName("state")
        public String state;
        @SerializedName("type")
        public String type;
        @SerializedName("house_no")
        public String house_no;
        @SerializedName("landmark")
        public String landmark;
        @SerializedName("street")
        public String street;
        @SerializedName("city")
        public String city;
        @SerializedName("id")
        public int id;
    }
}
