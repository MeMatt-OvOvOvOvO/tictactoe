package com.example.tiktaktoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class onlineActivity extends AppCompatActivity {


    private List<int[]> myList = new ArrayList<>();
    private TextView player1TextView, player2TextView;
    private int [] positions = {0,0,0,0,0,0,0,0,0};
    private List<String> doneBoxes = new ArrayList<>();
    private String[] boxesSelBy = {"","","","","","","","",""};
    private String turn = "";
    private int selectedBoxes = 1;
    private LinearLayout player1, player2;
    private ImageView one, two, three, four, five, six, seven, eight, nine;
    private String uniqueID = "0";
    private DatabaseReference dbref;
    private boolean enemyFound = false;
    private String enemyUniqueID = "0";
    private String status = "matching";
    private String connID = "";
    ValueEventListener turnsEventListener, wonEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);



        String getPlayerName = getIntent().getStringExtra("playerName");

        dbref = FirebaseDatabase.getInstance().getReference("path");
//        dbref = FirebaseDatabase.getInstance("https://tictactoe-d9638-default-rtdb.firebaseio.com").getReference("path");
        dbref.setValue(getPlayerName).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "poszło", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "nieposzło", Toast.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout1);
        rl.setBackgroundResource(R.drawable.nazwa);

        player1 = (LinearLayout) findViewById(R.id.player1);
        player2 = (LinearLayout) findViewById(R.id.player2);

        player1TextView = findViewById(R.id.player1TextView);
        player2TextView = findViewById(R.id.player2TextView);

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
        myList.add(new int[]{2,4,6});

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waitin' for the enemy!!");
        progressDialog.show();

        uniqueID = String.valueOf(System.currentTimeMillis());

        player1TextView.setText(getPlayerName);


        dbref.child("conns").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(enemyFound){
                    if(snapshot.hasChildren()){
                        for(DataSnapshot conns : snapshot.getChildren()){
                            String conID = conns.getKey();
                            int getPlayersCount = (int)conns.getChildrenCount();

                            if(status.equals("waiting")){
                                if(getPlayersCount == 2){
                                    turn = uniqueID;
                                    applyPlayerTurn(turn);

                                    boolean playerFound = false;
                                    for(DataSnapshot players : conns.getChildren()){
                                        String getPlayerUniqueID = players.getKey();

                                        if(getPlayerUniqueID.equals(uniqueID)){
                                            playerFound = true;
                                        }else if (playerFound){
                                            String getEnemyName = players.child("playerName").getValue(String.class);
                                            enemyUniqueID = players.getKey();
                                            player2TextView.setText(getEnemyName);

                                            connID = conID;
                                            enemyFound = true;

                                            dbref.child("turns").child(connID).addValueEventListener(turnsEventListener);
                                            dbref.child("won").child(connID).addValueEventListener(wonEventListener);

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                            dbref.child("conns").removeEventListener(this);
                                        }
                                    }
                                }
                            }else{
                                if(getPlayersCount == 1){
                                    conns.child(uniqueID).child("playerName").getRef().setValue(getPlayerName);

                                    for(DataSnapshot players : conns.getChildren()){
                                        String getEnemyName = players.child("playerName").getValue(String.class);
                                        enemyUniqueID = players.getKey();

                                        turn = enemyUniqueID;
                                        applyPlayerTurn(turn);

                                        player2TextView.setText(getEnemyName);
                                        connID = conID;
                                        enemyFound = true;

                                        dbref.child("turns").child(connID).addValueEventListener(turnsEventListener);
                                        dbref.child("won").child(connID).addValueEventListener(wonEventListener);

                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }

                                        dbref.child("conns").removeEventListener(this);
                                        break;
                                    }
                                }
                            }
                        }
                        if(!enemyFound && !status.equals("waiting")){
                            String connUniqID = String.valueOf(System.currentTimeMillis());
                            snapshot.child(connUniqID).child(uniqueID).child("player_Name").getRef().setValue(getPlayerName);
                            status = "waiting";
                        }

                    }else{
                        String connUniqID = String.valueOf(System.currentTimeMillis());
                        snapshot.child(connUniqID).child(uniqueID).child("player_Name").getRef().setValue(getPlayerName);
                        status = "waiting";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });

        turnsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getChildrenCount() == 2){
                        int getBoxPos = Integer.parseInt(dataSnapshot.child("boxPos").getValue(String.class));
                        String getPlayerID = dataSnapshot.child("playerID").getValue(String.class);
                        if(doneBoxes.contains(String.valueOf(getBoxPos))){
                            doneBoxes.add(String.valueOf(getBoxPos));
                            if(getBoxPos == 1){

                            }else if(getBoxPos == 2){

                            }else if(getBoxPos == 3){

                            }else if(getBoxPos == 4){

                            }else if(getBoxPos == 5){

                            }else if(getBoxPos == 6){

                            }else if(getBoxPos == 7){

                            }else if(getBoxPos == 8){

                            }else if(getBoxPos == 9){

                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        wonEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


    }

    private void applyPlayerTurn(String playerUniqueID){
        if(playerUniqueID.equals(uniqueID)){
            player1.setBackgroundResource(R.drawable.myshape);
            player2.setBackgroundResource(R.drawable.myshape1);
        }else{
            player1.setBackgroundResource(R.drawable.myshape1);
            player2.setBackgroundResource(R.drawable.myshape);
        }
    }

    private void selectBox(ImageView imageView, int selBoxPos, String selByPlayer){

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}