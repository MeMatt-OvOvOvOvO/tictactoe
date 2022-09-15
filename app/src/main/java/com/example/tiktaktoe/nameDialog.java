package com.example.tiktaktoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class nameDialog extends Dialog {

    private String name;
    private MainActivity mainActivity;

    public nameDialog(@NonNull Context context, String name, MainActivity mainActivity) {
        super(context);
        this.name = name;
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_dialog);
    }
}