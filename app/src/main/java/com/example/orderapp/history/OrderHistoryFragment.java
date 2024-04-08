package com.example.orderapp.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.orderapp.SharedPref;
import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.FragmentOrderHistoryBinding;
import com.example.orderapp.history.detail.OrderHistoryDetailActivity;
import com.example.orderapp.model.OrderHistoryModel;
import com.example.orderapp.model.UserData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class OrderHistoryFragment extends Fragment implements OrderHistoryAdapter.OnItemClickListener {

    private FragmentOrderHistoryBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserData userData;
    private OrderHistoryAdapter adapter;
    private List<OrderHistoryModel> list = new ArrayList<>();
    private DatabaseHandler databaseHandler;
    String[] country = {"Tất cả", "Chờ xác nhận", "Đã xác nhận", "Đang vận chuyển", "Đã hoàn thành"};


    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userData = new Gson().fromJson(SharedPref.read(SharedPref.USER_DATA, ""), UserData.class);
        databaseHandler = new DatabaseHandler(getContext());
        getListOrder();

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    list = databaseHandler.getAllOrder(userData.getId());
                    adapter.replace(list);
                } else {
                    getOrder(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(aa);
    }

    private void getOrder(int position) {
        List<OrderHistoryModel> listFilter = new ArrayList<>();
        list.forEach(new Consumer<OrderHistoryModel>() {
            @Override
            public void accept(OrderHistoryModel orderHistoryModel) {
                if (orderHistoryModel.getTrangthaidonhang() == position) {
                    listFilter.add(orderHistoryModel);
                }
                adapter.replace(listFilter);
            }
        });
    }

    private void getListOrder() {
        if (userData != null) {
            list = new ArrayList<>();
            list = databaseHandler.getAllOrder(userData.getId());
            adapter = new OrderHistoryAdapter(list);
            adapter.setOnItemClickListener(this);
            binding.recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(), OrderHistoryDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) list.get(position).getFoods());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}