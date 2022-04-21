package com.example.digirepo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class WelcomeUser extends AppCompatActivity {
    TextView uname,uid;
    Button button;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        uname = findViewById(R.id.userName);
        uid = findViewById(R.id.uniqueId);

        DocumentReference docRef = fStore.collection("users").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
//                    Toast.makeText(WelcomeUser.this, "inside exist ", Toast.LENGTH_SHORT).show();
                    String fname = documentSnapshot.getString("first");
                    String lname = documentSnapshot.getString("last");
                    String uId = documentSnapshot.getString("UID");
//                    Toast.makeText(WelcomeUser.this, "FirstName: "+fname+"LastName: "+lname, Toast.LENGTH_SHORT).show();
                    uname.setText(fname+" "+lname);
                    uid.setText(uId);
                }else {
//                    Toast.makeText(WelcomeUser.this, "inside else ", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WelcomeUser.this, "inside failure ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Screen4.class);
                startActivity(intent);
                finish();
            }
        });

        uid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= uid.getRight() - uid.getTotalPaddingRight()) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", uid.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(WelcomeUser.this, "UID Copied to Clipboard", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
                return true;
            }
        });

        button = findViewById(R.id.Next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendashboard();
            }
        });
    }
    public void opendashboard(){
        Intent intent = new Intent(this,dashboard.class);
        startActivity(intent);
    }



}