package in.mummysfood.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by acer on 9/10/2018.
 */

public class EntityModel
{


    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public Data data;

    public static class Data {
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

        public int user_id;
        public int entity_id;
        public String  entity_type;
    }
}
