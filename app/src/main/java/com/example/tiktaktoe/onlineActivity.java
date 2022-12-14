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
    private boolean enemyFound = false;
    private String enemyUniqueID = "0";
    private String status = "matching";
    private String connID = "";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tictactoe-d9638-default-rtdb.firebaseio.com");
    ValueEventListener turnsEventListener, wonEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);



        String getPlayerName = getIntent().getStringExtra("playerName");

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


        databaseReference.child("conns").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(!enemyFound){
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

                                            databaseReference.child("turns").child(connID).addValueEventListener(turnsEventListener);
                                            databaseReference.child("won").child(connID).addValueEventListener(wonEventListener);

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                            databaseReference.child("conns").removeEventListener(this);
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

                                        databaseReference.child("turns").child(connID).addValueEventListener(turnsEventListener);
                                        databaseReference.child("won").child(connID).addValueEventListener(wonEventListener);

                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }

                                        databaseReference.child("conns").removeEventListener(this);
                                        break;
                                    }
                                }
                            }
                        }
                        if(!enemyFound && !status.equals("waiting")){
                            String connUniqID = String.valueOf(System.currentTimeMillis());
                            snapshot.child(connUniqID).child(uniqueID).child("playerName").getRef().setValue(getPlayerName);
                            status = "waiting";
                        }

                    }else{
                        String connUniqID = String.valueOf(System.currentTimeMillis());
                        snapshot.child(connUniqID).child(uniqueID).child("playerName").getRef().setValue(getPlayerName);
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
                        if(!doneBoxes.contains(String.valueOf(getBoxPos))){
                            doneBoxes.add(String.valueOf(getBoxPos));
                            if(getBoxPos == 1){
                                selectBox(one, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 2){
                                selectBox(two, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 3){
                                selectBox(three, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 4){
                                selectBox(four, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 5){
                                selectBox(five, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 6){
                                selectBox(six, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 7){
                                selectBox(seven, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 8){
                                selectBox(eight, getBoxPos, getPlayerID);
                            }else if(getBoxPos == 9){
                                selectBox(nine, getBoxPos, getPlayerID);
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
                if(snapshot.hasChild("playerID")){
                    String getWinID = snapshot.child("playerID").getValue(String.class);
                    myDialog2 winDialog;
                    if(getWinID.equals(uniqueID)){
                        winDialog = new myDialog2(onlineActivity.this, "Congrats, You won!", onlineActivity.this);
                    }else{
                        winDialog = new myDialog2(onlineActivity.this, "Enemy won the game!", onlineActivity.this);
                    }
                    winDialog.setCancelable(false);
                    winDialog.show();

                    databaseReference.child("turns").child(connID).removeEventListener(turnsEventListener);
                    databaseReference.child("won").child(connID).removeEventListener(wonEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        one.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("1") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("1");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        two.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("2") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("2");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        three.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("3") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("3");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        four.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("4") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("4");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        five.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("5") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("5");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        six.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("6") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("6");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        seven.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("7") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("7");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        eight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("8") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("8");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

        nine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!doneBoxes.contains("9") && turn.equals(uniqueID)){
                    ((ImageView)view).setImageResource(R.drawable.cross);
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("boxPos").setValue("9");
                    databaseReference.child("turns").child(connID).child(String.valueOf(doneBoxes.size() + 1)).child("playerID").setValue(uniqueID);
                    turn = enemyUniqueID;
                }
            }
        });

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
        boxesSelBy[selBoxPos - 1] = selByPlayer;
        if(selByPlayer.equals(uniqueID)){
            imageView.setImageResource(R.drawable.circle);
            turn = enemyUniqueID;
        }else{
            imageView.setImageResource(R.drawable.cross);
            turn = uniqueID;
        }

        applyPlayerTurn(turn);

        if(checkPlayerWin(selByPlayer)){
            databaseReference.child("won").child(connID).child("playerID").setValue(selByPlayer);
        }
        if(doneBoxes.size() == 9){
            myDialog2 winDialog = new myDialog2(onlineActivity.this, "Result: Draw", onlineActivity.this);
            winDialog.setCancelable(false);
            winDialog.show();
        }
    }

    private boolean checkPlayerWin(String playerID){
        boolean playerWon = false;

        for(int i = 0; i < myList.size(); i++){
            int[] combination = myList.get(i);

            if(boxesSelBy[combination[0]].equals(playerID) && boxesSelBy[combination[1]].equals(playerID) && boxesSelBy[combination[2]].equals(playerID)){
                playerWon = true;
            }
        }
        return  playerWon;
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