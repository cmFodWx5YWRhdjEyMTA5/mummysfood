package in.mummysfood.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DashBoardModel implements Serializable {

 public double lat;
 public double lng;
 public String status;
 public List<Data>data;

 public static class Data implements Serializable{
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
  public Chef_detail chef_detail;
  public Food_detail food_detail;
  public boolean add_food;
  public int quantity;
  public String name;

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

 public static class Food_detail implements Serializable {

  @SerializedName("id")
  public int id;
  @SerializedName("user_id")
  public int user_id;
  @SerializedName("category_id")
  public int category_id;
  @SerializedName("name")
  public String name;
  @SerializedName("details")
  public String details;
  @SerializedName("media_type")
  public String media_type;
  @SerializedName("price")
  public String price;
  @SerializedName("created_at")
  public String created_at;
  @SerializedName("updated_at")
  public String updated_at;
  @SerializedName("month_price")
  public int month_price;
  @SerializedName("month_both_price")
  public int month_both_price;
  @SerializedName("month_lunch_price")
  public int month_lunch_price;
  @SerializedName("month_dinner_price")
  public int month_dinner_price;
  @SerializedName("week_price")
  public int week_price;
  @SerializedName("week_both_price")
  public int week_both_price;
  @SerializedName("week_lunch_price")
  public int week_lunch_price;
  @SerializedName("week_dinner_price")
  public int week_dinner_price;
  @SerializedName("taxes")
  public int taxes;
  @SerializedName("food_media")
  public List<Food_media> food_media;

  public static class Media {
   @SerializedName("id")
   public int id;
   @SerializedName("user_id")
   public int user_id;
   @SerializedName("name")
   public String name;
   @SerializedName("type")
   public String type;
   @SerializedName("source")
   public String source;
   @SerializedName("created_at")
   public String created_at;
   @SerializedName("updated_at")
   public String updated_at;
  }

  public static class Food_media {
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
   @SerializedName("media")
   public Media media;
  }
 }
}
