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

        TextView mess1 = findViewById(R.id.text);
        Button back = findViewById(R.id.backk);
        Button start = findViewById(R.id.startNeww);

        mess1.setText(mess);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                onActivity.startActivity(intent);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                getContext().startActivity(new Intent(getContext(), playerName.class));
                onActivity.finish();
            }
        });
    }
}