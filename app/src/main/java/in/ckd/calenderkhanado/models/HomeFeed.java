package in.ckd.calenderkhanado.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeFeed implements Serializable {

    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public List<Data> data;

    public static class User implements Serializable  {
        @SerializedName("id")
        public int id;
        @SerializedName("f_name")
        public String f_name;
        @SerializedName("l_name")
        public String l_name;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("email")
        public String email;
        @SerializedName("is_mobile_verified")
        public int is_mobile_verified;
        @SerializedName("is_email_verified")
        public int is_email_verified;
        @SerializedName("type")
        public String type;
        @SerializedName("profile_image")
        public String profile_image;
        @SerializedName("is_vagitarian")
        public int is_vagitarian;
        @SerializedName("device_id")
        public String device_id;
        @SerializedName("os")
        public String os;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("gender")
        public String gender;
        @SerializedName("login_type")
        public String login_type;
        @SerializedName("name")
        public String name;
    }

    public static class Addresses implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("city")
        public String city;
        @SerializedName("street")
        public String street;
        @SerializedName("landmark")
        public String landmark;
        @SerializedName("house_no")
        public String house_no;
        @SerializedName("type")
        public String type;
        @SerializedName("state")
        public String state;
        @SerializedName("pin")
        public String pin;
        @SerializedName("status")
        public String status;
        @SerializedName("nearby")
        public String nearby;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("complete_address")
        public String complete_address;
    }

    public static class Chef_detail implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("cooking_score")
        public int cooking_score;
        @SerializedName("want_to_learn")
        public int want_to_learn;
        @SerializedName("is_kitchen_varified")
        public int is_kitchen_varified;
        @SerializedName("about")
        public String about;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
    }

    public static class Food_media implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("media_id")
        public int media_id;
        @SerializedName("entity_media_id")
        public int entity_media_id;
        @SerializedName("entity_media_type")
        public String entity_media_type;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("media")
        public Media media;

        public static class Media implements Serializable  {
            @SerializedName("id")
            public int id;
            @SerializedName("user_id")
            public int user_id;
            @SerializedName("name")
            public String name;
            @SerializedName("type")
            public String type;
            @SerializedName("source")
            public String source;
            @SerializedName("created_at")
            public String created_at;
            @SerializedName("updated_at")
            public String updated_at;
        }
    }

    public static class User_media implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("media_id")
        public int media_id;
        @SerializedName("entity_media_id")
        public int entity_media_id;
        @SerializedName("entity_media_type")
        public String entity_media_type;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("media")
        public Media media;

        public static class Media implements Serializable {
            @SerializedName("id")
            public int id;
            @SerializedName("user_id")
            public int user_id;
            @SerializedName("name")
            public String name;
            @SerializedName("type")
            public String type;
            @SerializedName("source")
            public String source;
            @SerializedName("created_at")
            public String created_at;
            @SerializedName("updated_at")
            public String updated_at;
        }
    }

    public static class Data  implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("category_id")
        public int category_id;
        @SerializedName("name")
        public String name;
        @SerializedName("details")
        public String details;
        @SerializedName("food_type")
        public int food_type;
        @SerializedName("media_type")
        public String media_type;
        @SerializedName("price")
        public String price;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("options")
        public String options;
        @SerializedName("user")
        public User user;
        @SerializedName("addresses")
        public List<Addresses> addresses;
        @SerializedName("chef_detail")
        public Chef_detail chef_detail;
        @SerializedName("food_media")
        public List<Food_media> food_media;
        @SerializedName("user_media")
        public List<User_media> user_media;
        @SerializedName("month_price")
        public int month_price;
        @SerializedName("month_both_price")
        public int month_both_price;
        @SerializedName("month_lunch_price")
        public int month_lunch_price;
        @SerializedName("month_dinner_price")
        public int month_dinner_price;
        @SerializedName("week_price")
        public int week_price;
        @SerializedName("week_both_price")
        public int week_both_price;
        @SerializedName("week_lunch_price")
        public int week_lunch_price;
        @SerializedName("week_dinner_price")
        public int week_dinner_price;
    }
}
