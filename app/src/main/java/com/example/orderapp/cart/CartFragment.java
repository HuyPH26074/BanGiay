package com.example.orderapp.cart;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.orderapp.ProgressHelper;
import com.example.orderapp.R;
import com.example.orderapp.SharedPref;
import com.example.orderapp.ShowMessageHelper;
import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.FragmentCartBinding;
import com.example.orderapp.model.CartModel;
import com.example.orderapp.model.OrderModel;
import com.example.orderapp.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;


public class CartFragment extends Fragment implements CartAdapter.OnItemClickListener {
    private FragmentCartBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserData userData;
    private List<CartModel> list = new ArrayList<>();
    private CartAdapter adapter;
    private DatabaseHandler databaseHandler;

    int phuongthucthanhtoan = -1;

    public CartFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void getListCart() {
        if (userData != null) {
            list = databaseHandler.getAllCart(userData.getId());
            adapter = new CartAdapter(list);
            binding.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            binding.recyclerView.setHasFixedSize(true);
        }
        if (list.size() > 0) {
            binding.linear.setVisibility(View.VISIBLE);
            binding.btnOrder.setVisibility(View.VISIBLE);
        } else {
            binding.linear.setVisibility(View.GONE);
            binding.btnOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClick();
        databaseHandler = new DatabaseHandler(getContext());
        userData = new Gson().fromJson(SharedPref.read(SharedPref.USER_DATA, ""), UserData.class);
        binding.diachinhanhang.setText("Địa chỉ nhận hàng: " + userData.getAddress());
        binding.sodienthoai.setText("Số điện thoại: " + userData.getPhone());
        binding.nguoinhan.setText("Họ tên: " + userData.getFullName());
        list.clear();
        getListCart();
    }

    private void setOnClick() {
        binding.btnOrder.setOnClickListener(v -> {
            final boolean[] canOrder = new boolean[1];
            list.forEach(new Consumer<CartModel>() {
                @Override
                public void accept(CartModel cartModel) {
                    if (cartModel.isCheck()) {
                        canOrder[0] = true;
                    }
                }
            });
            if (canOrder[0]) {
                new AlertDialog.Builder(getContext()).setMessage("Bạn có chắc chắn muốn đặt hàng không ?").setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getListFood().forEach(new Consumer<CartModel>() {
                            @Override
                            public void accept(CartModel cartModel) {
                                if (databaseHandler.getSoLuongTrongKho(cartModel.getFoodID()) == 0) {
                                    Toast.makeText(getContext(), "Số lượng hàng trong kho đã hết", Toast.LENGTH_SHORT).show();
                                } else if (databaseHandler.getSoLuongTrongKho(cartModel.getFoodID()) < cartModel.getAmount()) {
                                    Toast.makeText(getContext(), "Không đủ số lượng " + cartModel.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    order();
                                }

                            }
                        });

                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();

            } else {
                ShowMessageHelper.showMessage(getContext(), "Bạn chưa chọn món ăn");
            }
        });
        binding.thanhToanKhiNhanHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phuongthucthanhtoan = 0;
            }
        });
        binding.thanhToanOnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phuongthucthanhtoan = 1;
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void order() {
        String date_order = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date());
        int userID = userData.getId();
        String data = new Gson().toJson(getListFood());
        long result = databaseHandler.createOrder(date_order, userID, data, phuongthucthanhtoan, userData.getAddress());
        if (result == -1) {
            ShowMessageHelper.showMessage(getContext(), "Order thất bại");
        } else {
            ShowMessageHelper.showMessage(getContext(), "Order thành công");
            List<CartModel> listCheck = getListFood();
            listCheck.forEach(cartModel -> {
                databaseHandler.removeCart(cartModel.getId());
                databaseHandler.updateSoLuong(cartModel.getFoodID(), cartModel.getAmount());
                list.remove(cartModel);
            });
            adapter.notifyDataSetChanged();
            if (list.size() > 0) {
                binding.linear.setVisibility(View.VISIBLE);
                binding.btnOrder.setVisibility(View.VISIBLE);
            } else {
                binding.linear.setVisibility(View.GONE);
                binding.btnOrder.setVisibility(View.GONE);
            }
        }
    }


    private List<CartModel> getListFood() {
        List<CartModel> listCheck = new ArrayList<>();
        if (list.size() > 0) {
            list.forEach(cartModel -> {
                if (cartModel.isCheck()) {
                    listCheck.add(cartModel);
                }
            });
        }
        return listCheck;
    }

    @Override
    public void onClick(CartModel cartModel) {
        if (cartModel != null) {
            new AlertDialog.Builder(getContext()).setMessage("Bạn có muốn xóa không ?").setPositiveButton("Có", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    removeCart(cartModel);
                    setTotalPrice();
                }
            }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
    }

    private void removeCart(CartModel cartModel) {
        if (cartModel != null) {
            int result = databaseHandler.removeCart(cartModel.getId());
            if (result == -1) {
                ShowMessageHelper.showMessage(getContext(), "Xóa giỏ hàng thất bại");
            } else {
                ShowMessageHelper.showMessage(getContext(), "Xóa giỏ hàng thành công");
                list.remove(cartModel);
                adapter.notifyDataSetChanged();
                if (list.size() > 0) {
                    binding.linear.setVisibility(View.VISIBLE);
                    binding.btnOrder.setVisibility(View.VISIBLE);
                } else {
                    binding.linear.setVisibility(View.GONE);
                    binding.btnOrder.setVisibility(View.GONE);
                }
            }
        }
    }

    private Long getTotalPrice() {
        long total = 0L;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck()) {
                total += list.get(i).getPrice() * list.get(i).getAmount();
            }
        }
        return total;
    }

    @Override
    public void setTotalPrice() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String totalPrice = "Tổng tiền: " + formatter.format(getTotalPrice()) + " VNĐ";
        binding.tvTotal.setText(totalPrice);
        binding.tvSoLuongChon.setText("Đã chọn: " + getListFood().size());
    }
}