package com.example.orderapp.updatefood;

import static com.example.orderapp.ShowMessageHelper.showMessage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.orderapp.ProgressHelper;
import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.ActivityUpdateFoodBinding;
import com.example.orderapp.model.FoodModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

public class UpdateFoodActivity extends AppCompatActivity {

    private ActivityUpdateFoodBinding binding;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private int documentID = -1;
    private FoodModel foodModel;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);
        documentID = (Integer) Objects.requireNonNull(getIntent().getExtras()).get("documentID");
        if (documentID != -1) {
            binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(binding.name.getText())) {
                        showMessage(UpdateFoodActivity.this, "Không được để trống tên");
                    } else if (TextUtils.isEmpty(binding.description.getText())) {
                        showMessage(UpdateFoodActivity.this, "Không được để trống mô tả");
                    } else if (TextUtils.isEmpty(binding.price.getText())) {
                        showMessage(UpdateFoodActivity.this, "Không được để trống giá");
                    } else if (TextUtils.isEmpty(binding.soluong.getText())) {
                        showMessage(UpdateFoodActivity.this, "Không được để số lượng");
                    } else {
                        int result = databaseHandler.updateProduct(binding.name.getText().toString(), binding.description.getText().toString(), Long.parseLong(removeDot(binding.price.getText().toString())), foodModel.getId(), Integer.parseInt(binding.soluong.getText().toString()));
                        if (result == -1) {
                            showMessage(UpdateFoodActivity.this, "Update thất bại");
                        } else {
                            showMessage(UpdateFoodActivity.this, "Update thành công");
                        }
                    }

                }
            });
        }

        foodModel = (FoodModel) Objects.requireNonNull(getIntent().getExtras()).get("data");
        if (foodModel != null) {
            binding.toolBarLayout.setTitle(foodModel.getName());
            Glide.with(this).load(foodModel.getImage()).into(binding.imgProduct);
            binding.name.setText(foodModel.getName());
            binding.description.setText(foodModel.getDescription());
            DecimalFormat formatter = new DecimalFormat("#,###");
            binding.price.setText(formatter.format(foodModel.getPrice()));
            binding.soluong.setText(foodModel.getSoluong() + "");
        }
    }

    private String removeDot(String text) {
        if (text.contains(".")) {
            return text.replace(".", "");
        }
        return text;
    }
}