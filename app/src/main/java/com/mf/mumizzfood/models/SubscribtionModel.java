package com.mf.mumizzfood.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by acer on 8/8/2018.
 */

public class SubscribtionModel implements Serializable {
    public String status;
    public List<Data> data;

    public static class Data implements Serializable{
        public int id;
        public int user_id;
        public int subscribe_to;
        public int number_of_days;
        public int ordered_plates;
        public int deliverd_order;
        public String status;
        public String created_at;
        public String updated_at;
    }
}
