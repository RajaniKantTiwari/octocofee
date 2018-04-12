package me.prateeksaigal.ocotocoffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Prateek Saigal on 29-10-2017.
 */

public class ProfileDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "profileDB";

    // Contacts table name
    private static final String TABLE_PROFILE = "profile";

    // Contacts Table Columns names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IMAGE = "image";



    public ProfileDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "("
                + KEY_USER_ID + " TEXT PRIMARY KEY," + KEY_EMAIL + " TEXT, " + KEY_PHONE + " TEXT, " + KEY_IMAGE + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void newSettings(String username, String email, String phone, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, username);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PHONE, phone);
        values.put(KEY_IMAGE, image);
        db.insert(TABLE_PROFILE, null, values);
        db.close();
    }

    public ArrayList<String> getSettings() {
        ArrayList<String> pair = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null){
            db.close();
            return null;
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {


            pair.add(cursor.getString(0));
            pair.add(cursor.getString(1));
            pair.add(cursor.getString(2));
            pair.add(cursor.getString(3));
            db.close();
            return pair;

        }
        db.close();
        return null;
    }

    public void deleteALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PROFILE);
        db.close();
    }




}
