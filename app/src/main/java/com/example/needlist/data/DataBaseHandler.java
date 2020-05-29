package com.example.needlist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.needlist.model.Needs;
import com.example.needlist.utils.Utils;

import java.util.ArrayList;


public class DataBaseHandler extends SQLiteOpenHelper {

    private static DataBaseHandler instance;

    public static DataBaseHandler getInstance(Context context) {
        if (instance == null){
            instance = new DataBaseHandler(context, Utils.DB_NAME);
        }
        return instance;
    }

    private DataBaseHandler(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE IF NOT EXISTS "
                + Utils.TABLE_NAME + "( " + Utils.KEY_ID + " INTEGER PRIMARY KEY, "
                + Utils.KEY_NAME + " TEXT, " + Utils.KEY_QUANTITY + " INTEGER, "
                + Utils.KEY_SIZE + " TEXT, " + Utils.KEY_NOTE + " TEXT" + ")";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP TABLE IF EXISTS " + Utils.TABLE_NAME;
        db.execSQL(drop);
        onCreate(db);
    }

    public void addToDb(Needs need){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utils.KEY_NAME,need.getName());
        values.put(Utils.KEY_QUANTITY,need.getQuantity());
        values.put(Utils.KEY_SIZE,need.getSize());
        values.put(Utils.KEY_NOTE,need.getNote());
        db.insert(Utils.TABLE_NAME,null,values);
        db.close();
    }

    public void updateElementOfDb(Needs need, int idInDb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utils.KEY_NAME,need.getName());
        values.put(Utils.KEY_QUANTITY,need.getQuantity());
        values.put(Utils.KEY_SIZE,need.getSize());
        values.put(Utils.KEY_NOTE,need.getNote());
        db.update(Utils.TABLE_NAME,
                values,
                Utils.KEY_ID + "=?",
                new String[]{String.valueOf(idInDb)});
        db.close();
    }

    public ArrayList<Needs> gettingAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Needs> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utils.TABLE_NAME,
                null,null);
        if (cursor.moveToLast()) {
            cursor.moveToLast();
            do {
                Needs need = new Needs();
                need.setId(cursor.getInt(cursor.getColumnIndex(Utils.KEY_ID)));
                need.setName(cursor.getString(cursor.getColumnIndex(Utils.KEY_NAME)));
                need.setQuantity(cursor.getInt(cursor.getColumnIndex(Utils.KEY_QUANTITY)));
                need.setSize(cursor.getString(cursor.getColumnIndex(Utils.KEY_SIZE)));
                need.setNote(cursor.getString(cursor.getColumnIndex(Utils.KEY_NOTE)));
                arrayList.add(need);
            } while (cursor.moveToPrevious());
            cursor.close();
            return arrayList;
        }
        db.close();
        return null;
    }

    public void deleteFromDb(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Utils.TABLE_NAME,Utils.KEY_ID + "=?",new String[]{String.valueOf(id)});
    }

}
