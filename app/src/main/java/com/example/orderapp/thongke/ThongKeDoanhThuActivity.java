package com.example.orderapp.thongke;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.orderapp.R;
import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.ActivityThongKeDoanhThuBinding;
import com.example.orderapp.model.CartModel;
import com.example.orderapp.model.OrderHistoryModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ThongKeDoanhThuActivity extends AppCompatActivity {

    private ActivityThongKeDoanhThuBinding binding;
    private DatabaseHandler databaseHandler;
    private List<OrderHistoryModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThongKeDoanhThuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHandler = new DatabaseHandler(this);
        DecimalFormat formatter = new DecimalFormat("#,### VNĐ");
        list = databaseHandler.getAllOrder();
        setSpannableStringBuilder("Số đơn hàng đã bán: ", list.size() + "", binding.textView4);
        setSpannableStringBuilder("Doanh thu: ", formatter.format(getTongTien()), binding.textView5);
        setSpannableStringBuilder("Số lượng sản phẩm đã bán: ", getSoLuongBanDuoc() + " sản phẩm", binding.textView6);

        setSpannableStringBuilder("Giày sneaker: ", getSoLuongGiayBanDuoc(0) + " sản phẩm", binding.textView7);
        setSpannableStringBuilder("Giày da: ", getSoLuongGiayBanDuoc(1) + " sản phẩm", binding.textView8);
        setSpannableStringBuilder("Giày thể thao: ", getSoLuongGiayBanDuoc(2) + " sản phẩm", binding.textView9);
        setSpannableStringBuilder("Giày lười: ", getSoLuongGiayBanDuoc(3) + " sản phẩm", binding.textView10);
    }

    public void setSpannableStringBuilder(String textStart, String textEnd, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1 = new SpannableString(textStart);
        str1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), 0);
        builder.append(str1);
        SpannableString str2 = new SpannableString(textEnd);
        str2.setSpan(new ForegroundColorSpan(Color.RED), 0, str2.length(), 0);
        builder.append(str2);
        textView.setText(builder, TextView.BufferType.SPANNABLE);
    }

    public int getTongTien() {
        final int[] tongtien = {0};
        list.forEach(orderHistoryModel -> {
            if (orderHistoryModel.getFoods() != null) {
                for (CartModel cartModel : orderHistoryModel.getFoods()) {
                    tongtien[0] += cartModel.getAmount() * cartModel.getPrice();
                }
            }
        });
        return tongtien[0];
    }

    public int getSoLuongBanDuoc() {
        final int[] slBanDUoc = {0};
        list.forEach(orderHistoryModel -> {
            if (orderHistoryModel.getFoods() != null) {
                for (CartModel cartModel : orderHistoryModel.getFoods()) {
                    slBanDUoc[0] += cartModel.getAmount();
                }
            }
        });
        return slBanDUoc[0];
    }

    public int getSoLuongGiayBanDuoc(int loaiGiay) {
        final int[] soluonggiay = {0};
        list.forEach(orderHistoryModel -> {
            if (orderHistoryModel.getFoods() != null) {
                for (CartModel cartModel : orderHistoryModel.getFoods()) {
                    if (cartModel.getLoaiGiay() == loaiGiay) {
                        soluonggiay[0] += cartModel.getAmount();
                    }
                }
            }
        });
        return soluonggiay[0];
    }
}