package com.example.blood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BloodDonorDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_DONORS = "donors";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BLOOD_GROUP = "blood_group";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_DONORS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_BLOOD_GROUP + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONORS);
        onCreate(db);
    }

    // Register a new donor with password hashing
    public boolean registerUser(String name, String email, String password, String bloodGroup, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_BLOOD_GROUP, bloodGroup);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashPassword(password)); // Encrypt password

        long result = db.insert(TABLE_DONORS, null, values);
        db.close();
        return result != -1;
    }

    // Check if an email is already registered
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_DONORS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Validate user login with password hashing check
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password); // Convert entered password to hash

        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_DONORS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, hashedPassword});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    // Search donors by blood type and location
    public Cursor searchDonors(String bloodType, String location) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DONORS + " WHERE " +
                COLUMN_BLOOD_GROUP + " LIKE ? AND " + COLUMN_LOCATION + " LIKE ?";
        return db.rawQuery(query, new String[]{"%" + bloodType + "%", "%" + location + "%"});
    }

    // Fetch all registered users (for debugging purposes)
    public void fetchAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DONORS, null);

        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                Log.d("DB_CHECK", "Email: " + email + ", Password: " + password);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    // Hash password using SHA-256 encryption
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // If hashing fails, return the plain password (not recommended)
        }
    }
}
