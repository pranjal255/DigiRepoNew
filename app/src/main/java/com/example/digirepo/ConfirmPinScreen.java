package com.example.digirepo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmPinScreen extends AppCompatActivity {
    EditText editText;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin_screen);
        editText = findViewById(R.id.inputcode4);
        button = findViewById(R.id.confirm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmpass();
            }
        });
        editText.addTextChangedListener(hide1);
    }
    public void confirmpass(){
        Intent intent = new Intent(this,WelcomeUser.class);
        startActivity(intent);
    }

    private TextWatcher hide1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text4 = editText.getText().toString().trim();
            button.setEnabled(!text4.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}