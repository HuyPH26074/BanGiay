package com.example.orderapp.quanlitheloai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.orderapp.ChiTietTheLoaiActivity;
import com.example.orderapp.R;
import com.example.orderapp.databinding.ActivityQuanLiTheLoaiBinding;

public class QuanLiTheLoaiActivity extends AppCompatActivity {

    private ActivityQuanLiTheLoaiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuanLiTheLoaiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnClick();
    }

    private void setOnClick() {
        binding.category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLiTheLoaiActivity.this, ChiTietTheLoaiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("loaiGiay", 0);
                bundle.putString("tenTheLoai", "Giày sneaker");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLiTheLoaiActivity.this, ChiTietTheLoaiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("loaiGiay", 1);
                bundle.putString("tenTheLoai", "Giày da");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLiTheLoaiActivity.this, ChiTietTheLoaiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("loaiGiay", 2);
                bundle.putString("tenTheLoai", "Giày thể thao");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        binding.category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLiTheLoaiActivity.this, ChiTietTheLoaiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("loaiGiay", 3);
                bundle.putString("tenTheLoai", "Giày lười");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}