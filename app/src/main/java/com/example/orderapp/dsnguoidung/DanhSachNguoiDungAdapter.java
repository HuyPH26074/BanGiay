package com.example.orderapp.dsnguoidung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderapp.R;
import com.example.orderapp.model.UserData;

import java.util.List;

public class DanhSachNguoiDungAdapter extends RecyclerView.Adapter<DanhSachNguoiDungAdapter.DsNguoiDungViewHolder> {

    private List<UserData> list;
    private TextView tentaikhoan;
    private TextView hovaten;
    private TextView diachi;
    private TextView sodienthoai;

    public DanhSachNguoiDungAdapter(List<UserData> list) {
        this.list = list;
    }

    public List<UserData> getList() {
        return list;
    }

    @NonNull
    @Override
    public DsNguoiDungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DsNguoiDungViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nguoidung, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DsNguoiDungViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class DsNguoiDungViewHolder extends RecyclerView.ViewHolder {

        public DsNguoiDungViewHolder(@NonNull View itemView) {
            super(itemView);
            tentaikhoan = itemView.findViewById(R.id.tentaikhoan);
            hovaten = itemView.findViewById(R.id.hovaten);
            diachi = itemView.findViewById(R.id.diachi);
            sodienthoai = itemView.findViewById(R.id.sodienthoai);
        }

        public void onBind(int position) {
            UserData userData = list.get(position);
            tentaikhoan.setText("Tên tài khoản: " + userData.getUserName());
            hovaten.setText("Họ tên: " + userData.getFullName());
            diachi.setText("Địa chỉ: " + userData.getAddress());
            sodienthoai.setText("Số điện thoại: " + userData.getPhone());
        }
    }

}
