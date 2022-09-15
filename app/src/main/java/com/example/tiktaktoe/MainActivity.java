package com.example.tiktaktoe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToOffline(View v)
    {
        Log.d("TAG", "dziala off ");
        Intent intent = new Intent(MainActivity.this, offlineActivity.class);
        startActivity(intent);
    }

    public void goToOnline(View v) {
        Log.d("TAG", "dziala on ");

        Intent intent1 = new Intent(MainActivity.this, playerName.class);
        startActivity(intent1);
    }

}