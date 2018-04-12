package me.prateeksaigal.ocotocoffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prateek Saigal on 29-10-2017.
 */

public class SettingsDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "settingsDB";

    // Contacts table name
    private static final String TABLE_SETTINGS = "settings";

    // Contacts Table Columns names
    private static final String KEY_PIN = "pin";
    private static final String KEY_ACC_ID = "accounting_id";


    public SettingsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_PIN + " TEXT PRIMARY KEY," + KEY_ACC_ID + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_SETTINGS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void newSettings(String pin, String acc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PIN, pin);
        values.put(KEY_ACC_ID, acc_id);
        db.insert(TABLE_SETTINGS, null, values);
        db.close();
    }

    public ArrayList<String> getSettings() {
        ArrayList<String> pair = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;

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
            db.close();
            return pair;

        }
        db.close();
        return null;
    }

    public void deleteALL() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SETTINGS);
        db.close();
    }

}
