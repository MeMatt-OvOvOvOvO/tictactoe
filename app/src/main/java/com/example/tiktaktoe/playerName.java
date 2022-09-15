package com.example.tiktaktoe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class playerName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.nazwa1);
        rl.setBackgroundResource(R.drawable.cos);

        EditText playerNamee = findViewById(R.id.playerName);
        AppCompatButton startGame = findViewById(R.id.startGame);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getPlayerName = playerNamee.getText().toString();

                if(getPlayerName.isEmpty()){
                    Toast.makeText(playerName.this,"Set nickname", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(playerName.this, onlineActivity.class);
                    intent.putExtra("playerName", getPlayerName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}