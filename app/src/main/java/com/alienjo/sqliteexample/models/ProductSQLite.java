package com.alienjo.sqliteexample.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductSQLite extends SQLiteOpenHelper {

    public static final String KEY_DB_NAME = "app_db";
    public static final int KEY_DB_VERSION = 1;

    public static final String KEY_TABLE_NAME = "products";
    public static final String KEY_ID = "p_id";
    public static final String KEY_NAME = "p_name";
    public static final String KEY_PRICE = "p_price";
    public static final String KEY_COUNT = "p_count";
    public static final String KEY_DESC = "p_desc";
    public static final String KEY_IMG = "p_img";


    public ProductSQLite(@Nullable Context context) {
        // create db
        super(context, KEY_DB_NAME, null, KEY_DB_VERSION);
    }

    // this function used to create db for the first time,
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + KEY_TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT, "
                + KEY_PRICE + " decimal(3,2), "
                + KEY_COUNT + " INTEGER, "
                + KEY_DESC + " TEXT,"
                + KEY_IMG + " TEXT)";

        //execSQL will execute our query !
        db.execSQL(query);

    }

    // this used to alter / update the db + tables structure
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void addProduct(Product newProduct) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, newProduct.getProductName());
        cv.put(KEY_PRICE, newProduct.getProductPrice());
        cv.put(KEY_DESC, newProduct.getProductDesc());
        cv.put(KEY_IMG, newProduct.getProductImg());
        cv.put(KEY_COUNT, newProduct.getProductCount());

        //getWritableDatabase this function allow you to write into sqlite db
        SQLiteDatabase db = getWritableDatabase();

        db.insert(KEY_TABLE_NAME, null, cv);

    }


    // read data from sqLite db and return them as arrayList
    public ArrayList<Product> getProductsArrayList() {

        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + KEY_TABLE_NAME;

        //getReadableDatabase allow you to read from sqLite
        SQLiteDatabase db = getReadableDatabase();

        // get data from sqLite db row bay row
        Cursor cursor = db.rawQuery(query, null);

        //check if there is data in table
        if (cursor.moveToFirst()) {

            do {
                Product p = new Product();

                String productName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String productDesc = cursor.getString(cursor.getColumnIndex(KEY_DESC));
                String productImg = cursor.getString(cursor.getColumnIndex(KEY_IMG));

                double productPrice = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                int productCount = cursor.getInt(cursor.getColumnIndex(KEY_COUNT));
                int productId = cursor.getInt(cursor.getColumnIndex(KEY_ID));

                p.setProductId(productId);
                p.setProductImg(productImg);
                p.setProductName(productName);
                p.setProductPrice(productPrice);
                p.setProductDesc(productDesc);
                p.setProductCount(productCount);

                products.add(p);

            } while (cursor.moveToNext());// check if there is rows available


        }

        return products;
    }


    public void updateProduct(Product newProduct) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAME, newProduct.getProductName());
        cv.put(KEY_PRICE, newProduct.getProductPrice());
        cv.put(KEY_DESC, newProduct.getProductDesc());
        cv.put(KEY_IMG, newProduct.getProductImg());
        cv.put(KEY_COUNT, newProduct.getProductCount());

//getWritableDatabase this function allow you to write into sqlite db
        SQLiteDatabase db = getWritableDatabase();

        db.update(KEY_TABLE_NAME, cv, KEY_ID + " = " + newProduct.getProductId(), null);

    }


    public void deleteProduct(int productId) {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(KEY_TABLE_NAME, KEY_ID + " = " + productId, null);
    }


}
