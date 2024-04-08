package com.example.orderapp;

import static com.example.orderapp.SharedPref.USER_DATA;
import static com.example.orderapp.ShowMessageHelper.showMessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.ActivityLoginBinding;
import com.example.orderapp.model.UserData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    private ActivityLoginBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);
        setOnClick();
        SharedPref.init(getApplicationContext());
    }

    private void setOnClick() {
        binding.btnLogin.setOnClickListener(view -> {
            String userName = Objects.requireNonNull(binding.edtUserName.getText()).toString();
            String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
            if (databaseHandler.login(userName, password)) {
                UserData userData = databaseHandler.getUserData(userName, password);
                SharedPref.write(USER_DATA, new Gson().toJson(userData));
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                this.finish();

            } else {
                showMessage(this, "Tên tài khoản hoặc mật khẩu không chính xác");
            }
        });
        binding.btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

    }
}