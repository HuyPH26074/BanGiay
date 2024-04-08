package com.example.orderapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.orderapp.ShowMessageHelper;
import com.example.orderapp.model.CartModel;
import com.example.orderapp.model.FoodModel;
import com.example.orderapp.model.OrderHistoryModel;
import com.example.orderapp.model.UserData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context context;

    public static final String DB_NAME = "order.db";

    public static final String TABLE_USER = "users";
    public static final String TABLE_PRODUCT = "product";
    public static final String TABLE_CART = "cart";
    public static final String TABLE_ORDER = "orders";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USER + "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "fullName" + " TEXT," + "userName" + " TEXT," + "password" + " TEXT," + "phone" + " TEXT," + "address" + " TEXT," + "role" + " TEXT" + ")";
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "name" + " TEXT," + "description" + " TEXT," + "price" + " LONG," + "image" + " TEXT, " + "loaiGiay" + " INTEGER, " + "soluong" + " INTEGER" + ")";
        String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "(" + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "amount" + " INTEGER," + "description" + " TEXT," + "foodID" + " INTEGER," + "image" + " TEXT," + "name" + " TEXT," + "price" + " LONG," + "userID" + " INTEGER, " + "loaiGiay INTEGER" + ")";
        String CREATE_TABLE_ORDER = "CREATE TABLE " + TABLE_ORDER + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "date_order TEXT, "
                + "userID INTEGER, "
                + "products TEXT, "
                + "phuongthucthanhtoan INTEGER, "
                + "diachinhanhang TEXT, "
                + "trangthaidonhang INTEGER"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_ADMIN);
        db.execSQL(CREATE_USER);
    }

    String CREATE_ADMIN = "INSERT INTO " + TABLE_USER + "(fullName, userName, password,phone,address,role) VALUES ('ho va ten admin', 'admin', 'admin',0987654321,'HaNoi','admin')";
    String CREATE_USER = "INSERT INTO " + TABLE_USER + "(fullName, userName, password,phone,address,role) VALUES ('ho va ten admin', 'tet123', '123',0987654321,'HaNoi','user')";

    public long createOrder(String date_order, int userID, String products, int phuongthucthanhtoan, String diachinhanhang) {
        //0 thanh toan khi nhan hang , 1 thanh toan online
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date_order", date_order);
        values.put("userID", userID);
        values.put("products", products);
        values.put("phuongthucthanhtoan", phuongthucthanhtoan);
        values.put("diachinhanhang", diachinhanhang);
        values.put("trangthaidonhang", 0);
        return db.insert(TABLE_ORDER, null, values);
    }

    public int updateTrangThaiDonHang(int id, int trangthaidonhang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthaidonhang", trangthaidonhang);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};
        return db.update(TABLE_ORDER, values, whereClause, whereArgs);
    }

    @SuppressLint("Range")
    public List<OrderHistoryModel> getAllOrder(int userID) {
        List<OrderHistoryModel> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ORDER + " WHERE userID = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userID)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String date_order = cursor.getString(cursor.getColumnIndex("date_order"));
                String products = cursor.getString(cursor.getColumnIndex("products"));
                String diachinhanhang = cursor.getString(cursor.getColumnIndex("diachinhanhang"));
                int phuongthucthanhtoan = cursor.getInt(cursor.getColumnIndex("phuongthucthanhtoan"));
                int trangthaidonhang = cursor.getInt(cursor.getColumnIndex("trangthaidonhang"));
                list.add(new OrderHistoryModel(id, phuongthucthanhtoan, date_order, diachinhanhang, new Gson().fromJson(products, new TypeToken<ArrayList<CartModel>>() {
                }.getType()), trangthaidonhang));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    public List<OrderHistoryModel> getAllOrder() {
        List<OrderHistoryModel> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String date_order = cursor.getString(cursor.getColumnIndex("date_order"));
                String diachinhanhang = cursor.getString(cursor.getColumnIndex("diachinhanhang"));
                String products = cursor.getString(cursor.getColumnIndex("products"));
                int phuongthucthanhtoan = cursor.getInt(cursor.getColumnIndex("phuongthucthanhtoan"));
                int trangthaidonhang = cursor.getInt(cursor.getColumnIndex("trangthaidonhang"));
                list.add(new OrderHistoryModel(id, phuongthucthanhtoan, date_order, diachinhanhang, new Gson().fromJson(products, new TypeToken<ArrayList<CartModel>>() {
                }.getType()), trangthaidonhang));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Recycle")
    public long addCart(int amount, String description, int foodID, String image, String name, Long price, int userID, int loaiGiay) {
        if (checkCart(userID, foodID)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("amount", getCurrentAmount(userID, foodID) + amount);
            String whereClause = "userID = ? AND foodID = ?";
            String[] whereArgs = {String.valueOf(userID), String.valueOf(foodID)};
            return db.update(TABLE_CART, values, whereClause, whereArgs);
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("amount", amount);
            values.put("description", description);
            values.put("foodID", foodID);
            values.put("image", image);
            values.put("name", name);
            values.put("price", price);
            values.put("userID", userID);
            values.put("loaiGiay", loaiGiay);
            return db.insert(TABLE_CART, null, values);
        }

    }

    public int removeCart(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_CART, "id=?", new String[]{String.valueOf(id)});
    }

    public void updateSoLuong(int idSanPham, int soLuongMua) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soluong", getSoLuongTrongKho(idSanPham) - soLuongMua);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(idSanPham)};
        db.update(TABLE_PRODUCT, values, whereClause, whereArgs);

    }

    @SuppressLint("Range")
    public int getSoLuongTrongKho(int idSanPham) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + "id =?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idSanPham)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("soluong"));
        }
        return 0;
    }

    @SuppressLint("Range")
    private int getCurrentAmount(int userID, int foodID) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_CART + " WHERE " + "userID =? AND foodID =?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userID), String.valueOf(foodID)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("amount"));
        }
        return 1;
    }

    public boolean checkCart(int userID, int productID) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + "userID" + " = ?" + " AND " + "foodID" + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID), String.valueOf(productID)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @SuppressLint("Range")
    public List<CartModel> getAllCart(int userID) {
        List<CartModel> listCart = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_CART + " WHERE" + " userID=?";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userID)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //int amount, String description, String foodID, String image, String name, Long price, int userID
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Long price = cursor.getLong(cursor.getColumnIndex("price"));
                int loaiGiay = cursor.getInt(cursor.getColumnIndex("loaiGiay"));
                int foodID = cursor.getInt(cursor.getColumnIndex("foodID"));
                CartModel cartModel = new CartModel(id, image, name, description, price, amount, loaiGiay,foodID);
                listCart.add(cartModel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listCart;
    }

    public long addProduct(FoodModel foodModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", foodModel.getName());
        values.put("description", foodModel.getDescription());
        values.put("price", foodModel.getPrice());
        values.put("image", foodModel.getImage());
        values.put("loaiGiay", foodModel.getLoaiGiay());
        values.put("soluong", foodModel.getSoluong());
        return db.insert(TABLE_PRODUCT, null, values);
    }


    public int updateProduct(String name, String description, Long price, int id,int soluong) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("soluong", soluong);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};
        return db.update(TABLE_PRODUCT, values, whereClause, whereArgs);
    }

    public int deleteProduct(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PRODUCT, "id=?", new String[]{String.valueOf(id)});
    }

    public List<FoodModel> getAll() {
        List<FoodModel> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_PRODUCT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                Long price = cursor.getLong(cursor.getColumnIndexOrThrow("price"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int loaiGiay = cursor.getInt(cursor.getColumnIndexOrThrow("loaiGiay"));
                int soluong = cursor.getInt(cursor.getColumnIndexOrThrow("soluong"));
                FoodModel foodModel = new FoodModel();
                foodModel.setSoluong(soluong);
                foodModel.setDescription(description);
                foodModel.setId(id);
                foodModel.setImage(image);
                foodModel.setLoaiGiay(loaiGiay);
                foodModel.setPrice(price);
                foodModel.setName(name);
                list.add(foodModel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void addUser(UserData userData) {
        if (isUsernameExists(userData.getUserName())) {
            ShowMessageHelper.showMessage(context, "Tên tài khoản đã tồn tại, Vui lòng sử dụng tên tài khoản khác");
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("fullName", userData.getFullName());
            values.put("userName", userData.getUserName());
            values.put("password", userData.getPassword());
            values.put("phone", userData.getPhone());
            values.put("address", userData.getAddress());
            values.put("role", userData.getRole());
            long result = db.insert(TABLE_USER, null, values);
            if (result == -1) {
                ShowMessageHelper.showMessage(context, "Đăng kí thất bại");
            } else {
                ShowMessageHelper.showMessage(context, "Đăng kí thành công");
            }
            db.close();
        }
    }

    public boolean isUsernameExists(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + "userName" + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean login(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + "userName" + " = ?" + " AND " + "password" + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userName, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    @SuppressLint({"Range", "Recycle"})
    public UserData getUserData(String userName, String password) {
        UserData userData = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USER + " WHERE " + "userName " + " = ?" + " AND " + "password" + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName, password});
        if (cursor.moveToFirst()) {
            userData = new UserData();
            userData.setId(cursor.getInt(cursor.getColumnIndex("id")));
            userData.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            userData.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
            userData.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            userData.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            userData.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            userData.setRole(cursor.getString(cursor.getColumnIndex("role")));
        }
        return userData;
    }

    @SuppressLint({"Range", "Recycle"})
    public UserData getUserData(int id) {
        UserData userData = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USER + " WHERE " + "id " + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            userData = new UserData();
            userData.setId(cursor.getInt(cursor.getColumnIndex("id")));
            userData.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            userData.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
            userData.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            userData.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            userData.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            userData.setRole(cursor.getString(cursor.getColumnIndex("role")));
        }
        return userData;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);

        onCreate(db);
    }


    public int updatePassWord(int id, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPass);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};
        return db.update(TABLE_USER, values, whereClause, whereArgs);
    }

    public int updateAddress(int id, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", address);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};
        return db.update(TABLE_USER, values, whereClause, whereArgs);
    }

    public int updatePhone(int id, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};
        return db.update(TABLE_USER, values, whereClause, whereArgs);
    }

    public int updateHoTen(int id, String hoten) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", hoten);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};
        return db.update(TABLE_USER, values, whereClause, whereArgs);
    }

    public List<FoodModel> getGiayTheoTheLoai(int loaiGiay) {
        List<FoodModel> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_PRODUCT + " WHERE loaiGiay = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(loaiGiay)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                Long price = cursor.getLong(cursor.getColumnIndexOrThrow("price"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int loaiGiay1 = cursor.getInt(cursor.getColumnIndexOrThrow("loaiGiay"));
                list.add(new FoodModel(image, name, description, price, id, loaiGiay1));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    @SuppressLint({"Range", "Recycle"})
    public List<UserData> getAllUser() {
        List<UserData> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                UserData userData = new UserData();
                userData.setId(cursor.getInt(cursor.getColumnIndex("id")));
                userData.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                userData.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
                userData.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                userData.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                userData.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                userData.setRole(cursor.getString(cursor.getColumnIndex("role")));
                list.add(userData);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }


}
