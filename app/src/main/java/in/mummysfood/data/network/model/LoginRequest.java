package in.mummysfood.data.network.model;

import in.mummysfood.models.DashBoardModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nilesh on 19/07/17.
 */

public class LoginRequest {


    public String status;
    public List<DashBoardModel.Data> data;
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
    public String os;
    public String created_at;
    public String updated_at;

  /*  public static class Data {
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
        public String os;
        public String created_at;
        public String updated_at;
    }*/
}
