package com.example.minisoria.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import com.example.minisoria.model.Cartitem;
import com.example.minisoria.model.Order;
import com.example.minisoria.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "UserDB.db";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 6);  // Updated DB version
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, email TEXT, password TEXT)");

        db.execSQL("CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "title TEXT," +
                "price TEXT," +
                "quantity INTEGER," +
                "material TEXT," +
                "imageResId INTEGER," +
                "status TEXT DEFAULT 'pending')");

        db.execSQL("CREATE TABLE messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "sender TEXT," +
                "receiver TEXT," +
                "text TEXT," +
                "image_uri TEXT," +
                "is_admin INTEGER," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE IF NOT EXISTS notifications (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "title TEXT," +
                "message TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            try {
                db.execSQL("ALTER TABLE messages ADD COLUMN is_admin INTEGER DEFAULT 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (oldVersion < 6) {
            db.execSQL("DROP TABLE IF EXISTS notifications");
            db.execSQL("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "title TEXT," +
                    "message TEXT," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean insertOrder(Cartitem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", item.getUsername());
        values.put("title", item.getTitle());
        values.put("price", item.getPrice());
        values.put("quantity", item.getQuantity());
        values.put("material", item.getMaterial());
        values.put("imageResId", item.getImageResId());
        values.put("status", "pending");
        long result = db.insert("orders", null, values);
        db.close();
        return result != -1;
    }

    public void insertNotification(String username, String title, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("title", title);
        values.put("message", message);
        db.insert("notifications", null, values);
        db.close();
    }

    // ✅ FIXED: use 'username' instead of 'user_id'
    public Cursor getNotificationsForUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM notifications WHERE username = ?";
        return db.rawQuery(query, new String[]{username});
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, username, title, price, quantity, material, status FROM orders", null);
        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(0);
                String userName = cursor.getString(1);
                String title = cursor.getString(2);
                String priceStr = cursor.getString(3);
                int quantity = cursor.getInt(4);
                String material = cursor.getString(5);
                String status = cursor.getString(6);

                double price = 0;
                try {
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                double totalPrice = price * quantity;
                orders.add(new Order(orderId, 0, userName, title, material, quantity, totalPrice, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

    public boolean updateOrderStatusById(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        int rows = db.update("orders", values, "id=?", new String[]{String.valueOf(orderId)});
        db.close();
        return rows > 0;
    }

    public boolean updateOrderStatus(String username, String title, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        int rows = db.update("orders", values, "username=? AND title=?", new String[]{username, title});
        db.close();
        return rows > 0;
    }

    public boolean deleteOrder(String username, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("orders", "username=? AND title=?", new String[]{username, title});
        db.close();
        return rows > 0;
    }

    public int getOrderCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM orders", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                User user = new User(username, email, password, "User");
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("users", "username=?", new String[]{username});
        db.close();
        return result > 0;
    }

    public boolean updateUser(String oldUsername, String newUsername, String newEmail, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newUsername);
        values.put("email", newEmail);
        values.put("password", newPassword);
        int rows = db.update("users", values, "username = ?", new String[]{oldUsername});
        db.close();
        return rows > 0;
    }

    public String[] getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"username", "email", "password"}, "username=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(0);
            String email = cursor.getString(1);
            String password = cursor.getString(2);
            cursor.close();
            db.close();
            return new String[]{name, email, password};
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }
    // Check if the username and email match
    public boolean validateUserForReset(String username, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND email=?", new String[]{username, email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Reset password
    public boolean resetPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("users", values, "username=?", new String[]{username});
        db.close();
        return rows > 0;
    }

}
