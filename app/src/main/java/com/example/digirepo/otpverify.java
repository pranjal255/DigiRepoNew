package com.example.digirepo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class otpverify extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private EditText inputcode1,inputcode2,inputcode3,inputcode4,inputcode5,inputcode6;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        TextView TextMobile = findViewById(R.id.TextMobile);
        TextMobile.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));

        inputcode1 = findViewById(R.id.inputcode1);
        inputcode2 = findViewById(R.id.inputcode2);
        inputcode3 = findViewById(R.id.inputcode3);
        inputcode4 = findViewById(R.id.inputcode4);
        inputcode5 = findViewById(R.id.inputcode5);
        inputcode6 = findViewById(R.id.inputcode6);//activated once text is written //and changes to next screen

        setupOTPInputs();
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final Button OTPVerify = findViewById(R.id.OTPVerify);

        verificationId = getIntent().getStringExtra("verificationId");

        OTPVerify.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(inputcode1.getText().toString().trim().isEmpty()
                        ||inputcode2.getText().toString().trim().isEmpty()
                        ||inputcode3.getText().toString().trim().isEmpty()
                        ||inputcode4.getText().toString().trim().isEmpty()
                        ||inputcode5.getText().toString().trim().isEmpty()
                        ||inputcode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(otpverify.this,"Please Enter Valid OTP",Toast.LENGTH_SHORT).show();
                    return;
                }
                String code=
                        inputcode1.getText().toString() +
                                inputcode2.getText().toString()+
                                inputcode3.getText().toString()+
                                inputcode4.getText().toString()+
                                inputcode5.getText().toString()+
                                inputcode6.getText().toString();
                System.out.println(verificationId);
                if(verificationId!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    OTPVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    fAuth.signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    OTPVerify.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        checkUserProfile();
//                                        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(otpverify.this,"The Entered OTP is invalid",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

        });
    }

    private void setupOTPInputs(){
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

//        @Override
//        protected void onStart() {
//            super.onStart();
//
//            if(fAuth.getCurrentUser() != null){
//                checkUserProfile();
//            }
//        }

    private void checkUserProfile() {
        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        Toast.makeText(otpverify.this, "testing "+fAuth.getCurrentUser().getUid()+fAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Intent intent = new Intent(getApplicationContext(),setpinscreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(otpverify.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
//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 Token for android studio login to github
