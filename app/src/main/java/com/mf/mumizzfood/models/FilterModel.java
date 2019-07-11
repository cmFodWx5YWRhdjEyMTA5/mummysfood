package com.mf.mumizzfood.models;

/**
 * Created by acer on 8/2/2018.
 */

public class FilterModel {
    public int filter_position;
    public String filter_name;
    public boolean active_filter;

    public FilterModel(int filter_position, String filter_name, boolean active_filter) {
        this.filter_position = filter_position;
        this.filter_name = filter_name;
        this.active_filter = active_filter;
    }
}
