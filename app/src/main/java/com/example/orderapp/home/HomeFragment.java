package com.example.orderapp.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.orderapp.DetailFoodActivity;
import com.example.orderapp.ProgressHelper;
import com.example.orderapp.SharedPref;
import com.example.orderapp.ShowMessageHelper;
import com.example.orderapp.ThemSanPhamActivity;
import com.example.orderapp.database.DatabaseHandler;
import com.example.orderapp.databinding.FragmentHomeBinding;
import com.example.orderapp.model.FoodModel;
import com.example.orderapp.model.UserData;
import com.example.orderapp.updatefood.UpdateFoodActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements HomeAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    private List<FoodModel> list = new ArrayList<>();
    private HomeAdapter adapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserData userData;
    private DatabaseHandler databaseHandler;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userData = new Gson().fromJson(SharedPref.read(SharedPref.USER_DATA, ""), UserData.class);
        databaseHandler = new DatabaseHandler(getContext());
        if (userData != null && !TextUtils.isEmpty(userData.getRole()) && userData.getRole().equals("admin")) {
            binding.imgAddFood.setVisibility(View.VISIBLE);
            binding.imgAddFood.setOnClickListener(v -> startActivity(new Intent(getContext(), ThemSanPhamActivity.class)));
        }


        binding.searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.tieude.setVisibility(View.GONE);
                }
            }
        });

        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.tieude.setVisibility(View.VISIBLE);
                return false;
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        ArrayList<FoodModel> filteredlist = new ArrayList<FoodModel>();
        for (FoodModel item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (!filteredlist.isEmpty()) {
            adapter.replaceList(filteredlist);
        }
    }

    private void getAllFoods() {
        list.clear();
        list = databaseHandler.getAll();
        adapter = new HomeAdapter(list);
        adapter.setOnItemClickListener(HomeFragment.this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(int position) {
        if (userData != null && !TextUtils.isEmpty(userData.getRole()) && userData.getRole().equals("admin")) {
            Intent intent = new Intent(requireContext(), UpdateFoodActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("documentID", list.get(position).getId());
            bundle.putSerializable("data", adapter.getList().get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), DetailFoodActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", adapter.getList().get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }

    @Override
    public void onDelete(FoodModel foodModel) {
        if (userData != null && !TextUtils.isEmpty(userData.getRole()) && userData.getRole().equals("admin")) {
            new AlertDialog.Builder(requireContext()).setMessage("Bạn có muốn xóa không ?").setPositiveButton("Có", (dialog, which) ->
                    deleteFood(foodModel)).setNegativeButton("Không", (dialog, which) -> {
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }

    }

    private void deleteFood(FoodModel foodModel) {
        int result = databaseHandler.deleteProduct(foodModel.getId());
        if (result == -1) {
            ShowMessageHelper.showMessage(getContext(), "Xóa thất bại");
        } else {
            list.remove(foodModel);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFoods();
    }
}
