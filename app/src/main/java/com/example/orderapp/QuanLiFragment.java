package com.example.orderapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.orderapp.databinding.FragmentQuanLiBinding;
import com.example.orderapp.dsnguoidung.DsNguoiDungActivity;
import com.example.orderapp.quanlitheloai.QuanLiTheLoaiActivity;
import com.example.orderapp.thongke.ThongKeDoanhThuActivity;

public class QuanLiFragment extends Fragment {

    private FragmentQuanLiBinding binding;

    public QuanLiFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuanLiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.quanlinguoidung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DsNguoiDungActivity.class);
                startActivity(intent);
            }
        });
        binding.quanlitheloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QuanLiTheLoaiActivity.class);
                startActivity(intent);
            }
        });
        binding.thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ThongKeDoanhThuActivity.class);
                startActivity(intent);
            }
        });
    }
}