package com.example.android.cardscorecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startPlayerlist(View v) {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
        finish();
    }

    public void startHowTo(View v) {

    }
}
