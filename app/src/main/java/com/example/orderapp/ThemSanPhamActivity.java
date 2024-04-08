package com.example.orderapp;

import static com.example.orderapp.ShowMessageHelper.showMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.ActivityThemSanPhamBinding;
import com.example.orderapp.model.FoodModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ThemSanPhamActivity extends AppCompatActivity {

    private ActivityThemSanPhamBinding binding;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String imageUpload;
    private DatabaseHandler databaseHandler;

    String[] country = {"Giày sneaker", "Giày da", "Giày thể thao", "Giày lười"};

    int loaiGiay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemSanPhamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnClick();
        databaseHandler = new DatabaseHandler(this);
        if (!isPerMissionReadImageSuccess()) {
            requestPerMission();
        }
    }

    private void setOnClick() {
        binding.imgProduct.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), 100);
        });

        binding.btnThemMonAn.setOnClickListener(view -> {

            if (TextUtils.isEmpty(imageUpload)) {
                showMessage(this, "Chưa có hình ảnh sản phẩm");
            } else if (TextUtils.isEmpty(binding.edtName.getText().toString())) {
                showMessage(this, "Chưa nhập tên sản phẩm");
            } else if (TextUtils.isEmpty(binding.edtDescription.getText().toString())) {
                showMessage(this, "Chưa nhập mô tả sản phẩm");
            } else if (TextUtils.isEmpty(binding.edtPrice.getText().toString())) {
                showMessage(this, "Chưa nhập giá sản phẩm");
            } else if (TextUtils.isEmpty(binding.edtSoLuong.getText().toString())) {
                showMessage(this, "Chưa nhập số lượng");
            } else {
                String image = imageUpload;
                String name = binding.edtName.getText().toString();
                String description = binding.edtDescription.getText().toString();
                Long price = Long.parseLong(binding.edtPrice.getText().toString());
                int soluong = Integer.parseInt(binding.edtSoLuong.getText().toString());

                FoodModel foodModel = new FoodModel();
                foodModel.setDescription(description);
                foodModel.setImage(image);
                foodModel.setLoaiGiay(loaiGiay);
                foodModel.setName(name);
                foodModel.setPrice(price);
                foodModel.setSoluong(soluong);
                long result = databaseHandler.addProduct(foodModel);
                if (result == -1) {
                    ShowMessageHelper.showMessage(this, "Thêm sản phẩm thất bại");
                } else {
                    ShowMessageHelper.showMessage(this, "Thêm sản phẩm thành công");
                }
            }


        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                loaiGiay = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(aa);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    binding.imgProduct.setImageURI(selectedImageUri);
                    binding.imgProduct.setVisibility(View.VISIBLE);
                    String imageName = "image_" + System.currentTimeMillis() + ".jpg";
                    upLoadImage(imageName, selectedImageUri);
                }
            }
        }
    }

    private void upLoadImage(String imageName, Uri imageUri) {
        ProgressHelper.showDialog(this, "");
        StorageReference imageRef = storageRef.child("images/" + imageName);
        // Tải hình ảnh lên Firebase Storage
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Hình ảnh đã được tải lên thành công
                ProgressHelper.dismissDialog();

                imageRef.getDownloadUrl().addOnSuccessListener(imageUri1 -> imageUpload = imageUri1.toString());
            }
        }).addOnFailureListener(e -> {
            // Xảy ra lỗi khi tải hình ảnh lên Firebase Storage
            ProgressHelper.dismissDialog();
        });
    }

    private boolean isPerMissionReadImageSuccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPerMission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

    }
}