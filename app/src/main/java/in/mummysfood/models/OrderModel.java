package in.mummysfood.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by acer on 8/8/2018.
 */

public class OrderModel {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
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

        public int subscribe_to;
        public int number_of_days;
        public int ordered_plates;
    }
}
