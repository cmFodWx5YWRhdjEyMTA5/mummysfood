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

  public int id;
  public int user_id;
  public int category_id;
  public String name;
  public String details;
  public String media_type;
  public String price;
  public String created_at;
  public String updated_at;
  public int month_price;
  public int month_both_price;
  public int month_lunch_price;
  public int month_dinner_price;
  public int week_price;
  public int week_both_price;
  public int week_lunch_price;
  public int week_dinner_price;
  public int taxes;
  public List<Food_media> food_media;

  public static class Media implements Serializable {
   public int id;
   public int user_id;
   public String name;
   public String type;
   public String source;
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
  }
 }
}
