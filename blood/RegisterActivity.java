package com.example.blood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etBloodGroup, etLocation;
    Button btnRegister, btnLogin;
    DatabaseHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etBloodGroup = findViewById(R.id.etBloodGroup);  // New field
        etLocation = findViewById(R.id.etLocation);      // New field
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String bloodGroup = etBloodGroup.getText().toString().trim();
            String location = etLocation.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || bloodGroup.isEmpty() || location.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkEmailExists(email)) {
                Toast.makeText(RegisterActivity.this, "Email already registered!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isInserted = db.registerUser(name, email, password, bloodGroup, location);

            if (isInserted) {
                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }
}
