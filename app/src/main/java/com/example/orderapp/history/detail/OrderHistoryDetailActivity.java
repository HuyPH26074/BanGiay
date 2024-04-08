package com.example.orderapp.history.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.orderapp.ProgressHelper;
import com.example.orderapp.R;
import com.example.orderapp.SharedPref;
import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.ActivityOrderHistoryDetailBinding;
import com.example.orderapp.model.CartModel;
import com.example.orderapp.model.OrderModel;
import com.example.orderapp.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistoryDetailActivity extends AppCompatActivity {

    private ActivityOrderHistoryDetailBinding binding;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private UserData userData;
    String documentID;
    List<CartModel> list = new ArrayList<>();
    private OrderHistoryDetailAdapter adapter;
    int trangthaidonhang = -1;
    private DatabaseHandler databaseHandler;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);
        userData = new Gson().fromJson(SharedPref.read(SharedPref.USER_DATA, ""), UserData.class);
        if (getIntent() != null && getIntent().getExtras() != null) {
            list = (List<CartModel>) getIntent().getExtras().get("data");
            id = getIntent().getExtras().getInt("id", -1);
            trangthaidonhang = getIntent().getExtras().getInt("trangthaidonhang", -1);
        }
        adapter = new OrderHistoryDetailAdapter(list);
        binding.recyclerView.setAdapter(adapter);


        binding.btnChuyenTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trangthaidonhang == -1) {
                    Toast.makeText(OrderHistoryDetailActivity.this, "Chưa chọn trạng thái muốn chuyển", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(OrderHistoryDetailActivity.this).setMessage("Bạn có muốn chuyển trạng thái đơn hàng không ?").setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateStatusOrder();
                        }


                    }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                }

            }
        });

        binding.radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trangthaidonhang = 1;
            }
        });
        binding.radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trangthaidonhang = 2;
            }
        });
        binding.radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trangthaidonhang = 3;
            }
        });

        if (userData.getRole().equals("user")) {
            binding.radioGroup.setVisibility(View.GONE);
            binding.textView2.setVisibility(View.GONE);
        }
        if (trangthaidonhang == 1) {
            binding.radio1.setChecked(true);
        } else if (trangthaidonhang == 2) {
            binding.radio2.setChecked(true);
        } else if (trangthaidonhang == 3) {
            binding.radio3.setChecked(true);
        }
    }

    private void updateStatusOrder() {
        int result = databaseHandler.updateTrangThaiDonHang(id, trangthaidonhang);
        if (result == -1) {
            Toast.makeText(OrderHistoryDetailActivity.this, "Chuyển trạng thái thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(OrderHistoryDetailActivity.this, "Chuyển trạng thái thành công", Toast.LENGTH_SHORT).show();

        }

    }
}