package com.mf.mumizzfood.models;


import java.io.Serializable;
import java.util.List;

public class HomeFeed implements Serializable {

    public String status;
    public List<Data> data;

    public static class User implements Serializable  {
        public int id;
        public String f_name;
        public String l_name;
        public String mobile;
        public String email;
        public int is_mobile_verified;
        public int is_email_verified;
        public String type;
        public String profile_image;
        public int is_vagitarian;
        public String device_id;
        public String os;
        public String created_at;
        public String updated_at;
        public String gender;
        public String login_type;
        public String name;
    }

    public static class Addresses implements Serializable {
        public int id;
        public String city;
        public String street;
        public String landmark;
        public String house_no;
        public String type;
        public String state;
        public String pin;
        public String status;
        public String nearby;
        public String created_at;
        public String updated_at;
        public int user_id;
        public double longitude;
        public double latitude;
        public String complete_address;
    }

    public static class Chef_detail implements Serializable {
        public int id;
        public int user_id;
        public int cooking_score;
        public int want_to_learn;
        public int is_kitchen_varified;
        public String about;
        public String created_at;
        public String updated_at;
    }

    public static class Food_media implements Serializable {
        public int id;
        public int media_id;
        public int entity_media_id;
        public String entity_media_type;
        public String created_at;
        public String updated_at;
        public Media media;

        public static class Media implements Serializable  {
            public int id;
            public int user_id;
            public String name;
            public String type;
            public String source;
            public String created_at;
            public String updated_at;
        }
    }

    public static class User_media implements Serializable {
        public int id;
        public int media_id;
        public int entity_media_id;
        public String entity_media_type;
        public String created_at;
        public String updated_at;
        public Media media;

        public static class Media implements Serializable {
            public int id;
            public int user_id;
            public String name;
            public String type;
            public String source;
            public String created_at;
            public String updated_at;
        }
    }

    public static class Data  implements Serializable {
        public int id;
        public int user_id;
        public int category_id;
        public String name;
        public String details;
        public String food_type;
        public String media_type;
        public String price;
        public String created_at;
        public String updated_at;
        public String options;
        public User user;
        public List<Addresses> addresses;
        public Chef_detail chef_detail;
        public List<Food_media> food_media;
        public List<User_media> user_media;

        public int weekly_lunch_price;
        public int weekly_dinner_price;
        public int monthly_lunch_price;
        public int monthly_dinner_price;
    }
}
