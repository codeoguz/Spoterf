package com.example.freespotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

public class ContestCreationActivity extends AppCompatActivity {

    DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference contestRef = mDataBase.child("contest");

    TextView codeTxt;
    EditText contestCode, parameter1, parameter2, hardness, emailAddress, password;
    Button createContestBtn, generateCodeBtn;

    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_creation);

        initializeFields();

        generateCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCode();
            }
        });

        createContestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContest();
            }
        });

    }

    private void initializeFields() {

        codeTxt = findViewById(R.id.codeTxt);
        contestCode = findViewById(R.id.contestCode);
        parameter1 = findViewById(R.id.parameter_1);
        parameter2 = findViewById(R.id.parameter_2);
        createContestBtn = findViewById(R.id.createContest);
        generateCodeBtn = findViewById(R.id.generateCode);
        hardness = findViewById(R.id.hardness);
        emailAddress = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edittext);
    }


    private void createContest(){
        if(code.isEmpty() || contestCode.getText().toString().isEmpty() || emailAddress.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter all the parameters", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> dataHashMap = new HashMap();
            dataHashMap.put("contestCode", contestCode.getText().toString());
            dataHashMap.put("email", emailAddress.getText().toString());
            dataHashMap.put("password", password.getText().toString());
            dataHashMap.put("encryptedCode", code);
            dataHashMap.put("hardness", hardness.getText().toString());
            dataHashMap.put("isFinished", "0");
            dataHashMap.put("parameter1", parameter1.getText().toString());
            dataHashMap.put("parameter2", parameter2.getText().toString());
            contestRef.child(contestCode.getText().toString()).setValue(dataHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(ContestCreationActivity.this, "Unsuccessful Attempt", Toast.LENGTH_SHORT).show();
                        startHomeActiviy();
                    } else{
                        startContestActivity();
                    }
                }
            });
        }
    }

    private void startContestActivity() {
        Intent i = new Intent(this, ContestsActivity.class);
        startActivity(i);
        finish();
    }

    private void startHomeActiviy() {
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }

    private void generateCode() {

        if(parameter1.getText().toString().isEmpty() || parameter2.getText().toString().isEmpty() || hardness.getText().toString().isEmpty()){

            Toast.makeText(this, "Enter all the parameters!", Toast.LENGTH_SHORT).show();
        } else {

            contestCode.setText(createRandomCode(5));
            code = generateCode.createEncryptedCode(parameter1.getText().toString(), parameter2.getText().toString(), Integer.parseInt(hardness.getText().toString()));
            codeTxt.setText(code);
        }
    }

    public String createRandomCode(int codeLength){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        System.out.println(output);
        return output ;
    }
}