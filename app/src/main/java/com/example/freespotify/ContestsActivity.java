package com.example.freespotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class ContestsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    DatabaseReference contestsDataBase;
    FirebaseUser user;



    private EditText nameEditText, emailEditText, parameterOne, parameterTwo;
    private TextView codeTxt, contestCodeTxt, attemptsDisplay, contestWinnerName, contestName, enterContestCode, joinTxt, contest_to_earn_more;
    private Button checkCode, parametersConfirm;
    private RelativeLayout winnerLayout;
    private ImageView closeWinnerTab;


    private int attempts = 0;
    String name, emailAddress, contestCode, parameter1, parameter2, code, isFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contests);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        contestsDataBase = mDataBase.child("contest");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        initializeFields();

        nameEditText.setText(user.getDisplayName());
        emailEditText.setText(user.getEmail());

        closeWinnerTab();

        checkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contestCode = contestCodeTxt.getText().toString();

                contestsDataBase.child(contestCode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            if (contestCode.isEmpty()){
                                Toast.makeText(ContestsActivity.this, "Enter Contest Code", Toast.LENGTH_SHORT).show();
                            } else {

                                contestsDataBase.child(contestCode).child("users").child(user.getUid()).child("winner").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            youHaveWon();
                                        } else {
                                            contestsDataBase.child(contestCode).child("winner").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                    if(snapshot.exists()){
                                                        Toast.makeText(ContestsActivity.this, "You are too late, better luck next time :)", Toast.LENGTH_SHORT).show();
                                                        Log.i("isThereAWinner", "Yes, there is.");
                                                    } else {

                                                        Log.i("isThereAWinner", "No, there isn't.");
                                                        getAttempts();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });



                            }
                        } else {
                            Toast.makeText(ContestsActivity.this, "Enter a proper code.", Toast.LENGTH_SHORT).show();
                            Log.i("checkCode", "contestCode not found.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


            }
        });

        parametersConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get real parameter values
                getParameters();

                contestsDataBase.child(contestCode).child("winner").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            if (attempts > 0){
                                isWon();
                                setAttempts();
                            } else {
                                Toast.makeText(ContestsActivity.this, "You Don't Have Any Attempts Left", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ContestsActivity.this, "You are too late, better luck next time :)", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void closeWinnerTab() {
        closeWinnerTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomeActiviy();
            }
        });
    }

    private void startHomeActiviy() {
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }

    private void initializeFields() {
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        codeTxt = findViewById(R.id.codeTxt);
        parametersConfirm = findViewById(R.id.parameters_confirm_btn);
        contestCodeTxt = findViewById(R.id.contestCodeTxt);
        attemptsDisplay = findViewById(R.id.attemptsDisplay);
        checkCode = findViewById(R.id.checkContestCode);
        parameterOne = findViewById(R.id.parameter_1);
        parameterTwo = findViewById(R.id.parameter_2);
        contestName = findViewById(R.id.contest_name);
        contestWinnerName = findViewById(R.id.winnerName);
        winnerLayout = findViewById(R.id.winnerLayout);
        enterContestCode = findViewById(R.id.enter_contest_code);
        joinTxt = findViewById(R.id.joinTxt);
        contest_to_earn_more = findViewById(R.id.contest_to_earn_more);
        closeWinnerTab = findViewById(R.id.closeWinnerTab);
    }

    private void makeInvisible(){
        enterContestCode.setVisibility(View.GONE);
        nameEditText.setVisibility(View.GONE);
        emailEditText.setVisibility(View.GONE);
        codeTxt.setVisibility(View.GONE);
        attemptsDisplay.setVisibility(View.GONE);
        parametersConfirm.setVisibility(View.GONE);
        contestCodeTxt.setVisibility(View.GONE);
        checkCode.setVisibility(View.GONE);
        parameterOne.setVisibility(View.GONE);
        parameterTwo.setVisibility(View.GONE);
        joinTxt.setVisibility(View.GONE);
        contest_to_earn_more.setVisibility(View.GONE);

    }

    private void youHaveWon(){

        contestName.setText(contestCode);
        contestWinnerName.setText(user.getDisplayName());

        makeInvisible();

        winnerLayout.setVisibility(View.VISIBLE);
    }


    private void getAttempts() {
            contestsDataBase.child(contestCode).child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Log.i("Snapshot Ref", String.valueOf(snapshot.getRef()));

                    if(snapshot.exists()){
                        attempts = Integer.parseInt(String.valueOf(snapshot.child("attempts").getValue()));
                        attemptsDisplay.setText(String.valueOf(attempts));
                        Log.i("Already Joined", "User is already joined this contest.");
                        Log.i("getAttempts", String.valueOf(attempts));

                        if (attempts > 0){
                            generateCodes();
                        } else {
                            Toast.makeText(ContestsActivity.this, "You Don't Have Any Attempts Left", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        joinUserToContest();
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
    }

    private void setAttempts() {

        contestsDataBase.child(String.valueOf(contestCode)).child("users").child(String.valueOf(user.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    contestsDataBase.child(String.valueOf(contestCode)).child("users").child(String.valueOf(user.getUid())).child("attempts").setValue("3");
                } else {

                    contestsDataBase.child(String.valueOf(contestCode)).child("users").child(String.valueOf(user.getUid())).child("attempts").setValue(String.valueOf(attempts));
                }
                Log.i("setAttempts", String.valueOf(attempts));
                attemptsDisplay.setText(String.valueOf(attempts));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void joinUserToContest() {

        contestsDataBase.child(contestCode).child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.i("joinUserToContest", "Data not found");
                    contestsDataBase.child(contestCode).child("users").child(String.valueOf(user.getUid())).child("attempts").setValue(String.valueOf("3"));
                } else {
                    Log.i("joinUserToContest", "User is already joined.");
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    String realParameter1, realParameter2;

    private void isWon() {

        if(parameterOne.getText().toString().isEmpty() || parameterTwo.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter all the parameters!", Toast.LENGTH_SHORT).show();
        } else {

            parameter1 = parameterOne.getText().toString();
            parameter2 = parameterTwo.getText().toString();

            if(parameter1.equals(realParameter1) || parameter2.equals(realParameter2)){
                contestWinner();
                attempts--;
            } else if (parameter2.equals(realParameter1) || parameter1.equals(realParameter2)){
                contestWinner();
                attempts--;
            } else {
                wrongAnswer();
                attempts--;
            }
            }
        }


    private void wrongAnswer() {

        Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
    }

    private void contestWinner() {

        emailAddress = emailEditText.getText().toString();
        name = nameEditText.getText().toString();

        contestsDataBase.child(contestCode).child("winner").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.exists()){

                    Log.i("contestWinner", "winner is " + name);

                    contestsDataBase.child(contestCode).child("winner").child("email").setValue(emailAddress);
                    contestsDataBase.child(contestCode).child("winner").child("name").setValue(name);
                    contestsDataBase.child(contestCode).child("users").child(user.getUid()).child("winner").setValue("1");

                    youHaveWon();
                } else {
                    Toast.makeText(ContestsActivity.this, "You are too late, better luck next time :)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getParameters() {
        contestsDataBase.child(contestCode).child("parameter1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    realParameter1 = snapshot.getValue().toString();
                } else {
                    Toast.makeText(ContestsActivity.this, "Data is not found...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        contestsDataBase.child(contestCode).child("parameter2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    realParameter2 = snapshot.getValue().toString();

                } else {
                    Toast.makeText(ContestsActivity.this, "Data is not found...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    private void generateCodes() {

        contestCode = contestCodeTxt.getText().toString();

        if (contestCode.isEmpty()){
            Toast.makeText(this, "Enter Contest Code", Toast.LENGTH_SHORT).show();
        } else {

            contestsDataBase.child(contestCode).child("encryptedCode").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        code = String.valueOf(snapshot.getValue());
                        codeTxt.setText(code);
                    } else {
                        Toast.makeText(ContestsActivity.this, "Data is not found...", Toast.LENGTH_SHORT).show();
                        codeTxt.setText("NO CODE");
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }
    }

    private void setData() {

        if(nameEditText.getText().toString().isEmpty() || emailEditText.getText().toString().isEmpty() || contestCodeTxt.getText().toString().isEmpty() || parameterOne.getText().toString().isEmpty() || parameterTwo.getText().toString().isEmpty()){


        }

        name = nameEditText.getText().toString();
        emailAddress = emailEditText.getText().toString();
        contestCode = contestCodeTxt.getText().toString();
        parameter1 = parameterOne.getText().toString();
        parameter2 = parameterTwo.getText().toString();
    }




}