package com.example.orderapp.profile;

import static com.example.orderapp.SharedPref.USER_DATA;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderapp.LoginActivity;
import com.example.orderapp.ProgressHelper;
import com.example.orderapp.R;
import com.example.orderapp.SharedPref;
import com.example.orderapp.ShowMessageHelper;
import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.FragmentProfileBinding;
import com.example.orderapp.model.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserData userData;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private DatabaseHandler databaseHandler;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userData = new Gson().fromJson(SharedPref.read(SharedPref.USER_DATA, ""), UserData.class);
        databaseHandler = new DatabaseHandler(getContext());
        if (userData != null) {
            binding.accountName.setText("Tài khoản: " + userData.getUserName());
            binding.address.setText("Địa chỉ: " + userData.getAddress());
            binding.phone.setText("Số điện thoại: " + userData.getPhone() + "");
            binding.fullName.setText("Họ và tên: " + userData.getFullName());
        }
        binding.tvDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(requireContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_layout_alert, null);
                Button button = dialogView.findViewById(R.id.btnDoiMK);
                TextInputEditText edtNewPass = dialogView.findViewById(R.id.edtNewPass);
                TextInputEditText edtOldPass = dialogView.findViewById(R.id.edtPassOld);
                TextInputEditText edtConfirmNewPass = dialogView.findViewById(R.id.edtConfirmNewPass);
                button.setOnClickListener(v1 -> {
                    updatePassword(Objects.requireNonNull(edtOldPass.getText()).toString(), Objects.requireNonNull(edtNewPass.getText()).toString(), Objects.requireNonNull(edtConfirmNewPass.getText()).toString());
                });
                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        binding.tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.tvDoiHoten.setOnClickListener(view1 -> {
            dialogBuilder = new AlertDialog.Builder(requireContext());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_layout_alert2, null);
            TextInputEditText hovaten = dialogView.findViewById(R.id.hovaten);
            dialogView.findViewById(R.id.btnDoiMK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(hovaten.getText().toString())) {
                        Toast.makeText(getContext(), "Chưa nhập họ và tên", Toast.LENGTH_SHORT).show();
                    } else {
                        int result = databaseHandler.updateHoTen(userData.getId(), hovaten.getText().toString());
                        if (result == -1) {
                            Toast.makeText(getContext(), "Cập nhật họ và tên thất bại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật họ và tên thành công", Toast.LENGTH_SHORT).show();
                            updateUserData();
                        }
                    }
                }
            });
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
        binding.tvDoiDiaChi.setOnClickListener(view1 -> {
            dialogBuilder = new AlertDialog.Builder(requireContext());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_layout_alert3, null);
            TextInputEditText hovaten = dialogView.findViewById(R.id.diachi);
            dialogView.findViewById(R.id.btnDoiMK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(hovaten.getText().toString())) {
                        Toast.makeText(getContext(), "Chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                    } else {
                        int result = databaseHandler.updateAddress(userData.getId(), hovaten.getText().toString());
                        if (result == -1) {
                            Toast.makeText(getContext(), "Cập nhật địa chỉ thất bại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật địa chỉ thành công", Toast.LENGTH_SHORT).show();
                            updateUserData();
                            ;
                        }
                    }
                }
            });
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
        binding.tvDoiSoDienThoai.setOnClickListener(view1 -> {
            dialogBuilder = new AlertDialog.Builder(requireContext());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_layout_alert4, null);
            TextInputEditText hovaten = dialogView.findViewById(R.id.sodienthoai);
            dialogView.findViewById(R.id.btnDoiMK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(hovaten.getText().toString())) {
                        Toast.makeText(getContext(), "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
                    } else {
                        int result = databaseHandler.updatePhone(userData.getId(), hovaten.getText().toString());
                        if (result == -1) {
                            Toast.makeText(getContext(), "Cập nhật số điện thoại thất bại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật số điện thoại thành công", Toast.LENGTH_SHORT).show();
                            updateUserData();
                            ;
                        }
                    }
                }
            });
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        });
    }

    private void updatePassword(String oldPass, String newPass, String confirmPassNew) {
        if (oldPass.equals("") || newPass.equals("") || confirmPassNew.equals("")) {
            ShowMessageHelper.showMessage(getContext(), "Chưa nhập đủ mật khẩu");
        } else if (!oldPass.equals(userData.getPassword())) {
            ShowMessageHelper.showMessage(getContext(), "Mật khẩu cũ chưa đúng");
        } else if (!newPass.equals(confirmPassNew)) {
            ShowMessageHelper.showMessage(getContext(), "Mật khẩu nhắc lại chưa đúng");
        } else {
            int result = databaseHandler.updatePassWord(userData.getId(), newPass);
            if (result == -1) {
                Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                updateUserData();
            }
        }

    }

    private void updateUserData() {
        UserData userDataNew = databaseHandler.getUserData(userData.getId());
        SharedPref.write(USER_DATA, new Gson().toJson(userDataNew));
        userData = new Gson().fromJson(SharedPref.read(SharedPref.USER_DATA, ""), UserData.class);
        if (userData != null) {
            binding.accountName.setText("Tài khoản: " + userData.getUserName());
            binding.address.setText("Địa chỉ: " + userData.getAddress());
            binding.phone.setText("Số điện thoại: " + userData.getPhone() + "");
            binding.fullName.setText("Họ và tên: " + userData.getFullName());
        }
    }
}