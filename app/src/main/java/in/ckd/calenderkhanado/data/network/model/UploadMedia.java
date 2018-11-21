package in.ckd.calenderkhanado.data.network.model;

import com.google.gson.annotations.SerializedName;

public class UploadMedia {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public String type;
        @SerializedName("source")
        public String source;
        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("id")
        public int id;
    }
}
