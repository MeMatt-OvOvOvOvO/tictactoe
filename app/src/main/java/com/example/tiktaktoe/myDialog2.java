package com.example.tiktaktoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class myDialog2 extends Dialog {
    private String mess;
    private onlineActivity onActivity;

    public myDialog2(@NonNull Context context, String mess, onlineActivity onActivity) {
        super(context);
        this.mess = mess;
        this.onActivity = onActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dialog2);

        TextView text = findViewById(R.id.text);

        Button backButton = findViewById(R.id.back);
        Button startnew = findViewById(R.id.startNew);

        text.setText(mess);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                onActivity.startActivity(intent);
            }
        });

        startnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActivity.restart();
                dismiss();
            }
        });
    }
}