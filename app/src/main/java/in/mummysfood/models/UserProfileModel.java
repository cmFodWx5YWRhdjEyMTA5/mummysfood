package in.mummysfood.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by acer on 8/8/2018.
 */

public class UserProfileModel {
    public String status;
    public List<Data> data;

    public static class Addresses
    {
    }

    public static class Orders {
        public int id;
        public int food_user_id;
        public int order_by;
        public int order_for;
        public String food_detail;
        public String food_name;
        public String chef_name;
        public int subscribe_id;
        public String house_no;
        public String landmark;
        public String street;
        public String city;
        public String state;
        public String pincode;
        public String address_type;
        public String price;
        public String delivery_charge;
        public int quantity;
        public String payment_status;
        public int is_order_confirmed;
        public String pick_at;
        public String deliver_at;
        public String status;
        public String created_at;
        public String updated_at;
        public int user_id;
    }

    public static class Subscribes {
        public int id;
        public int user_id;
        public int subscribe_to;
        public int number_of_days;
        public int ordered_plates;
        public String status;
        public String created_at;
        public String updated_at;
        public List<Orders> orders;
    }

    public static class Data {
        public int id;public String f_name;
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
        public List<Addresses> addresses;
        public String food_detail;
        public String chef_detail;
        public List<Subscribes> subscribes;
    }
}
