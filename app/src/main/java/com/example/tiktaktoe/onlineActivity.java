package com.example.tiktaktoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class onlineActivity extends AppCompatActivity {
    private List<int[]> myList = new ArrayList<>();

    private int [] positions = {0,0,0,0,0,0,0,0,0};
    private int turn = 1;
    private int selectedBoxes = 1;
    private LinearLayout player1, player2;
    private ImageView one, two, three, four, five, six, seven, eight, nine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout1);
        rl.setBackgroundResource(R.drawable.nazwa);

        player1 = (LinearLayout) findViewById(R.id.player1);
        player2 = (LinearLayout) findViewById(R.id.player2);

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);

        myList.add(new int[]{0,1,2});
        myList.add(new int[]{3,4,5});
        myList.add(new int[]{6,7,8});
        myList.add(new int[]{0,3,6});
        myList.add(new int[]{1,4,7});
        myList.add(new int[]{2,5,8});
        myList.add(new int[]{0,4,8});

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(0)){
                    action((ImageView) view, 0);
                }
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(1)){
                    action((ImageView) view, 1);
                }
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(2)){
                    action((ImageView) view, 2);
                }
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(3)){
                    action((ImageView) view, 3);
                }
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(4)){
                    action((ImageView) view, 4);
                }
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(5)){
                    action((ImageView) view, 5);
                }
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(6)){
                    action((ImageView) view, 6);
                }
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(7)){
                    action((ImageView) view, 7);
                }
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectable(8)){
                    action((ImageView) view, 8);
                }
            }
        });
    }

    private void action(ImageView imageView, int selectedPositions){
        positions[selectedPositions] = turn;

        if(turn == 1){
            imageView.setImageResource(R.drawable.circle);

            if(playerWin()){

                myDialog2 winDialog = new myDialog2(onlineActivity.this, "Player 1 has won the match", onlineActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            }
            else if(selectedBoxes == 9){
                myDialog2 winDialog = new myDialog2(onlineActivity.this, "Result: draw", onlineActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            }
            else{
                changeTurn(2);
                selectedBoxes++;
            }
        }
        else{
            imageView.setImageResource(R.drawable.cross);

            if(playerWin()){
                myDialog2 winDialog = new myDialog2(onlineActivity.this, "Player 2 has won the match", onlineActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            }
            else if(selectedBoxes == 9){
                myDialog2 winDialog = new myDialog2(onlineActivity.this, "Result: draw", onlineActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            }
            else{
                changeTurn(1);
                selectedBoxes++;
            }
        }
    }

    private void changeTurn(int currTurn){
        turn = currTurn;
        if(turn == 1){
            player1.setBackgroundResource(R.drawable.myshape);
            player2.setBackgroundResource(R.drawable.myshape1);
        }else{
            player1.setBackgroundResource(R.drawable.myshape1);
            player2.setBackgroundResource(R.drawable.myshape);

        }
    }

    private boolean playerWin(){
        boolean res = false;

        for(int i=0;i<myList.size();i++){
            int [] combin = myList.get(i);

            if(positions[combin[0]] == turn && positions[combin[1]] == turn && positions[combin[2]] == turn){
                res = true;
            }
        }
        return res;
    }

    private boolean selectable(int position){
        boolean res = false;
        if(positions[position] == 0){
            res = true;
        }
        return res;
    }

    public void restart(){
        positions = new int[]{0,0,0,0,0,0,0,0,0};

        turn = 1;
        selectedBoxes = 1;
        one.setImageResource(R.drawable.myshape2);
        two.setImageResource(R.drawable.myshape2);
        three.setImageResource(R.drawable.myshape2);
        four.setImageResource(R.drawable.myshape2);
        five.setImageResource(R.drawable.myshape2);
        six.setImageResource(R.drawable.myshape2);
        seven.setImageResource(R.drawable.myshape2);
        eight.setImageResource(R.drawable.myshape2);
        nine.setImageResource(R.drawable.myshape2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigateUpTo() {
    }

    public void startActivity() {
    }
}