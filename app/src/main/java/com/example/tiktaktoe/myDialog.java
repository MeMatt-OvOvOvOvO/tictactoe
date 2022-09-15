package com.example.tiktaktoe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class myDialog extends Dialog {

    private String mess;
    private offlineActivity offActivity;

    public myDialog(@NonNull Context context, String mess, offlineActivity offActivity) {
        super(context);
        this.mess = mess;
        this.offActivity = offActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.win_dialog);

        TextView text = findViewById(R.id.text);

        Button backButton = findViewById(R.id.back);
        Button startnew = findViewById(R.id.startNew);

        text.setText(mess);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                offActivity.startActivity(intent);
            }
        });

        startnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offActivity.restart();
                dismiss();
            }
        });

    }
}
