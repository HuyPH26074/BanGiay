package com.example.orderapp.theloai;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.orderapp.ChiTietTheLoaiActivity;
import com.example.orderapp.R;
import com.example.orderapp.databinding.FragmentTheLoaiBinding;


public class TheLoaiFragment extends Fragment {

    FragmentTheLoaiBinding binding;

    public TheLoaiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTheLoaiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClick();
    }

    private void setOnClick() {
        binding.category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChiTietTheLoaiActivity.class);
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
                Intent intent = new Intent(getContext(), ChiTietTheLoaiActivity.class);
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
                Intent intent = new Intent(getContext(), ChiTietTheLoaiActivity.class);
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
                Intent intent = new Intent(getContext(), ChiTietTheLoaiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("loaiGiay", 3);
                bundle.putString("tenTheLoai", "Giày lười");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}