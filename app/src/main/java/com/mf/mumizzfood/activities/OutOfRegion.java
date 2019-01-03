package com.mf.mumizzfood.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mf.mumizzfood.R;

public class OutOfRegion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_of_region);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
