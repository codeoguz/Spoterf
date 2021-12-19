package com.example.freespotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private static final String TAG = "Home";
    private static GoogleSignInAccount account;
    private static GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDataBase;
    DatabaseReference usersRef;

    int isAdmin;

    Boolean isMenuOpen;
    private static ImageView menuBtn;
    private static TextView joinContestBtn;
    public TextView signOut, newContest, closeMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        
        mDataBase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDataBase.child("users");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        initializeFields();

        ListView musicsListView = (ListView) findViewById(R.id.musics_listview);

        joinContestBtn = findViewById(R.id.join_contest_btn);

        //Check if the user is admin...
        isAdmin = MainActivity.isAdmin;
        Log.i("isAdmin", String.valueOf(isAdmin));



        joinContestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startContestActivity();
            }
        });


        isMenuOpen = false;
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuManagement();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        closeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuManagement();
            }
        });

        newContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startContestCreationActivity();
            }
        });

        // Create music and add them to the list.
        //
        music atesonmus = new music("Ateş Sönmüş", "K4N");
        music atesonmus2 = new music("Sular buhar", "doguzkaan ft. K4N");
        music atesonmus3 = new music("Ateş Sönmüş", "K4N");
        music atesonmus4 = new music("Ateş Sönmüş", "K4N");
        ArrayList<music> musicArrayList = new ArrayList<>();
/*        musicArrayList.add(atesonmus);
        musicArrayList.add(atesonmus2);
        musicArrayList.add(atesonmus3);
        musicArrayList.add(atesonmus4);
        musicArrayList.add(atesonmus);
        musicArrayList.add(atesonmus2);
        musicArrayList.add(atesonmus3);
        musicArrayList.add(atesonmus4);
        musicArrayList.add(atesonmus);
        musicArrayList.add(atesonmus2);
        musicArrayList.add(atesonmus3);
        musicArrayList.add(atesonmus4);
        musicArrayList.add(atesonmus);
        musicArrayList.add(atesonmus2);
        musicArrayList.add(atesonmus3);
        musicArrayList.add(atesonmus4);*/
        MusicListAdapter adapter = new MusicListAdapter(this, R.layout.song_layout, musicArrayList);
        musicsListView.setAdapter(adapter);
        //



    }



    private void initializeFields() {
        signOut = findViewById(R.id.sign_out_btn);
        newContest = findViewById(R.id.newContestItem);
        closeMenu = findViewById(R.id.closeMenu);
        menuBtn = findViewById(R.id.menu_btn);
    }

    private void menuManagement(){
        if(isMenuOpen){
            makeInvisible();
            isMenuOpen = false;
        } else {
            makeVisible();
            isMenuOpen = true;
        }
    }


    private void makeVisible(){


        if(isAdmin == 1){
            newContest.setVisibility(View.VISIBLE);
        }
        signOut.setVisibility(View.VISIBLE);
        closeMenu.setVisibility(View.VISIBLE);
    }

    private void makeInvisible(){
        newContest.setVisibility(View.GONE);
        signOut.setVisibility(View.GONE);
        closeMenu.setVisibility(View.GONE);
    }

    private void startSignInActivity(){
        Intent i = new Intent(this, signInActivity.class);
        startActivity(i);

    }

    private void startContestActivity(){

        Intent i = new Intent(this, ContestsActivity.class);
        startActivity(i);
    }

    private void startContestCreationActivity(){
        Intent i = new Intent(this, ContestCreationActivity.class);
        startActivity(i);
    }

    private static Boolean isTrue = false;

    private void signOut() {
        mAuth.signOut();
        startSignInActivity();
    }
}