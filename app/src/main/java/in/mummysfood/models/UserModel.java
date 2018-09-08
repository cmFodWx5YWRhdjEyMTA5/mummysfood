package in.mummysfood.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by acer on 6/17/2018.
 */

public class UserModel {


    public String userName;
    public String email;


    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public List<Data> data;

    public static class Addresses {
    }



    public static class Data {
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
        @SerializedName("addresses")
        public List<Addresses> addresses;
        @SerializedName("orders")
        public List<Orders> orders;
        @SerializedName("food_detail")
        public String food_detail;
        @SerializedName("chef_detail")
        public String chef_detail;
        @SerializedName("subscribes")
        public List<Subscribes> subscribes;
    }


    public static class Orders {
        @SerializedName("id")
        public int id;
        @SerializedName("food_user_id")
        public int food_user_id;
        @SerializedName("order_by")
        public int order_by;
        @SerializedName("order_for")
        public int order_for;
        @SerializedName("food_detail")
        public String food_detail;
        @SerializedName("food_name")
        public String food_name;
        @SerializedName("chef_name")
        public String chef_name;
        @SerializedName("subscribe_id")
        public int subscribe_id;
        @SerializedName("house_no")
        public String house_no;
        @SerializedName("landmark")
        public String landmark;
        @SerializedName("street")
        public String street;
        @SerializedName("city")
        public String city;
        @SerializedName("state")
        public String state;
        @SerializedName("pincode")
        public String pincode;
        @SerializedName("address_type")
        public String address_type;
        @SerializedName("price")
        public String price;
        @SerializedName("delivery_charge")
        public String delivery_charge;
        @SerializedName("quantity")
        public int quantity;
        @SerializedName("payment_status")
        public String payment_status;
        @SerializedName("is_order_confirmed")
        public int is_order_confirmed;
        @SerializedName("pick_at")
        public String pick_at;
        @SerializedName("deliver_at")
        public String deliver_at;
        @SerializedName("status")
        public String status;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("user_id")
        public int user_id;
    }

    public static class Subscribes {
        @SerializedName("id")
        public int id;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("subscribe_to")
        public int subscribe_to;
        @SerializedName("number_of_days")
        public int number_of_days;
        @SerializedName("ordered_plates")
        public int ordered_plates;
        @SerializedName("status")
        public String status;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("updated_at")
        public String updated_at;
    }


    public UserModel(String name, String emailUser) {
        this.userName = name;
        this.email = emailUser;
    }

}
