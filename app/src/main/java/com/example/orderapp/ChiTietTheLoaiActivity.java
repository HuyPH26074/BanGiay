package com.example.orderapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.ActivityChiTietTheLoaiBinding;
import com.example.orderapp.home.HomeAdapter;
import com.example.orderapp.home.HomeFragment;
import com.example.orderapp.model.FoodModel;
import com.example.orderapp.model.UserData;
import com.example.orderapp.updatefood.UpdateFoodActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChiTietTheLoaiActivity extends AppCompatActivity implements HomeAdapter.OnItemClickListener {

    private DatabaseHandler databaseHandler;
    private List<FoodModel> list = new ArrayList<>();

    private int loaiGiay = 0;
    private String tenTheLoai;
    private ActivityChiTietTheLoaiBinding binding;
    private HomeAdapter adapter;
    private UserData userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietTheLoaiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);
        userData = new Gson().fromJson(SharedPref.read(SharedPref.USER_DATA, ""), UserData.class);
        loaiGiay = Objects.requireNonNull(getIntent().getExtras()).getInt("loaiGiay", -1);
        tenTheLoai = Objects.requireNonNull(getIntent().getExtras()).getString("tenTheLoai", "");
        if (tenTheLoai != null) {
            binding.tvTenTheLoai.setText(tenTheLoai);
        }
        list = databaseHandler.getGiayTheoTheLoai(loaiGiay);
        adapter = new HomeAdapter(list);
        adapter.setOnItemClickListener(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(int position) {
        if (userData != null && !TextUtils.isEmpty(userData.getRole()) && userData.getRole().equals("admin")) {
            Intent intent = new Intent(this, UpdateFoodActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("documentID", list.get(position).getId());
            bundle.putSerializable("data", adapter.getList().get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DetailFoodActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", adapter.getList().get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onDelete(FoodModel foodModel) {
        if (userData != null && !TextUtils.isEmpty(userData.getRole()) && userData.getRole().equals("admin")) {
            new AlertDialog.Builder(this).setMessage("Bạn có muốn xóa không ?").setPositiveButton("Có", (dialog, which) ->
                    deleteFood(foodModel)).setNegativeButton("Không", (dialog, which) -> {
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

    private void deleteFood(FoodModel foodModel) {
        int result = databaseHandler.deleteProduct(foodModel.getId());
        if (result == -1) {
            ShowMessageHelper.showMessage(this, "Xóa thất bại");
        } else {
            list.remove(foodModel);
            adapter.notifyDataSetChanged();
        }
    }
}