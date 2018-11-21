package in.ckd.calenderkhanado.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by acer on 8/8/2018.
 */

public class SubscribtionModel {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
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
}
