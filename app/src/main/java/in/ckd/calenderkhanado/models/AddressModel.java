package in.ckd.calenderkhanado.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by acer on 8/2/2018.
 */

public class AddressModel {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
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
        public String complete_address;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("latitude")
        public double latitude;


    }
}
