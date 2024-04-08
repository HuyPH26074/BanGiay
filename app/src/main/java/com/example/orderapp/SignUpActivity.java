package com.example.orderapp;

import static com.example.orderapp.ShowMessageHelper.showMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.ActivitySignUpBinding;
import com.example.orderapp.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);
        setOnClick();
    }

    private void setOnClick() {
        binding.btnSignUp.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.edtUserName.getText())) {
                showMessage(this, "Không được để trống họ và tên");
            } else if (TextUtils.isEmpty(binding.edtFullName.getText())) {
                showMessage(this, "Không được để trống tài khoản");
            } else if (TextUtils.isEmpty(binding.edtPassword.getText())) {
                showMessage(this, "Không được để trống mật khẩu");
            } else if (TextUtils.isEmpty(binding.edtPhone.getText())) {
                showMessage(this, "Không được để trống số điện thoại");
            } else if (TextUtils.isEmpty(binding.edtAddress.getText())) {
                showMessage(this, "Không được để trống địa chỉ");
            } else {
                SignUp();
            }
        });
    }

    private void SignUp() {
        UserData user = new UserData();
        user.setRole("user");
        user.setFullName(Objects.requireNonNull(binding.edtFullName.getText()).toString());
        user.setUserName(Objects.requireNonNull(binding.edtUserName.getText()).toString());
        user.setPassword(Objects.requireNonNull(binding.edtPassword.getText()).toString());
        user.setPhone(Objects.requireNonNull(binding.edtPhone.getText()).toString());
        user.setAddress(Objects.requireNonNull(binding.edtAddress.getText()).toString());
        databaseHandler.addUser(user);
    }

}