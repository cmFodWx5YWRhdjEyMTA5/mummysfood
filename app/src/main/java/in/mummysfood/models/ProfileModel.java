package in.mummysfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProfileModel implements Serializable {


    public List<Data> data;
    public String status;

    public static class Data implements Serializable{
        public Chef_open_days chef_open_days;
        public List<String> subscribes;
        public Chef_detail chef_detail;
        public List<Food_detail>food_detail;
        public List<Addresses> addresses;
        public String name;
        public String login_type;
        public String gender;
        public String updated_at;
        public String created_at;
        public int is_vagitarian;
        public String profile_image;
        public String device_id;
        public String os;
        public String type;
        public int is_email_verified;
        public int is_mobile_verified;
        public String email;
        public String mobile;
        public String l_name;
        public String f_name;
        public int id;
    }

    public static class Chef_open_days implements Serializable{
        public String sun_close_hr;
        public String sun_open_hr;
        public String sat_close_hr;
        public String sat_open_hr;
        public String fri_close_hr;
        public String fri_open_hr;
        public String thus_close_hr;
        public String thus_open_hr;
        public String wed_close_hr;
        public String wed_open_hr;
        public String tues_close_hr;
        public String tues_open_hr;
        public String mon_close_hr;
        public String mon_open_hr;
        public String user_id;
        public int id;
    }

    public static class Chef_detail implements Serializable{
        public String updated_at;
        public String created_at;
        public String about;
        public int is_kitchen_varified;
        public int want_to_learn;
        public int cooking_score;
        public int user_id;
        public int id;
        public List<Chef_media> chef_media;
    }

    public static class Food_detail implements Serializable {
        public List<Food_media> food_media;
        public String price;
        public String media_type;
        public String details;
        public String name;
        public int category_id;
        public int user_id;
        public int id;
    }

    public static class Food_media implements Serializable{
        public Media media;
        public String updated_at;
        public String created_at;
        public String entity_media_type;
        public int entity_media_id;
        public int media_id;
        public int id;
    }

    public static class Chef_media implements Serializable{
        public Media media;
        public String updated_at;
        public String created_at;
        public String entity_media_type;
        public int entity_media_id;
        public int media_id;
        public int id;
    }

    public static class Media implements Serializable{
        public String updated_at;
        public String created_at;
        public String source;
        public String type;
        public String name;
        public int user_id;
        public int id;
    }

    public static class Addresses implements Serializable {
        public double latitude;
        public double longitude;
        public int user_id;
        public String updated_at;
        public String created_at;
        public String nearby;
        public String status;
        public String pin;
        public String state;
        public String type;
        public String house_no;
        public String landmark;
        public String street;
        public String city;
        public int id;
    }
}
