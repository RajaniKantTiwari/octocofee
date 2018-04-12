package me.prateeksaigal.ocotocoffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Prateek Saigal on 01-11-2017.
 */

public class CoffeeDBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "coffeeManager";

    // Contacts table name
    private static final String TABLE_COFFEE = "coffee";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COFFEE = "coffee";
    private static final String KEY_MILK = "milk";
    private static final String KEY_FOAM = "foam";
    private static final String KEY_LIQUID1 = "liquid1";
    private static final String KEY_LIQUID2 = "liquid2";
    private static final String KEY_TYPE = "type";



    public CoffeeDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_COFFEE = "CREATE TABLE " + TABLE_COFFEE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_COFFEE + " TEXT,"
                + KEY_MILK + " TEXT,"
                + KEY_FOAM + " TEXT,"
                + KEY_LIQUID1 + " TEXT,"
                + KEY_LIQUID2 + " TEXT,"
                + KEY_TYPE + " Integer" + ")";

        sqLiteDatabase.execSQL(CREATE_COFFEE);

        Coffee coffee = new Coffee("Latte", "60", "50", "80", "1", "2", 1);
        addDefaultRows(sqLiteDatabase, coffee);
        coffee = new Coffee("Cappuccino", "40", "50", "90", "1", "2", 2);
        addDefaultRows(sqLiteDatabase, coffee);
        coffee = new Coffee("Americano", "80", "50", "20", "1", "2", 3);
        addDefaultRows(sqLiteDatabase, coffee);
        coffee = new Coffee("Americano", "30", "50", "700", "1", "2", 4);
        addDefaultRows(sqLiteDatabase, coffee);

    }

    public void addDefaultRows(SQLiteDatabase db, Coffee coffee) {


        ContentValues values = new ContentValues();
        values.put(KEY_NAME, coffee.getName()); // Contact Name
        values.put(KEY_COFFEE, coffee.get_coffee()); // Contact Name
        values.put(KEY_FOAM, coffee.get_foam()); // Contact Name
        values.put(KEY_MILK, coffee.get_milk()); // Contact Name
        values.put(KEY_LIQUID1, coffee.get_liquid1()); // Contact Name
        values.put(KEY_LIQUID2, coffee.get_liquid2()); // Contact Name
        values.put(KEY_TYPE, coffee.get_type());

        // Inserting Row
        db.insert(TABLE_COFFEE, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COFFEE);

        // Create tables again
        onCreate(sqLiteDatabase);

    }

    public void addNew(Coffee coffee) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, coffee.getName()); // Contact Name
        values.put(KEY_COFFEE, coffee.get_coffee()); // Contact Name
        values.put(KEY_FOAM, coffee.get_foam()); // Contact Name
        values.put(KEY_MILK, coffee.get_milk()); // Contact Name
        values.put(KEY_LIQUID1, coffee.get_liquid1()); // Contact Name
        values.put(KEY_LIQUID2, coffee.get_liquid2()); // Contact Name
        values.put(KEY_TYPE, coffee.get_type());


        // Inserting Row
        db.insert(TABLE_COFFEE, null, values);
        db.close(); // Closing database connection
    }


    public ArrayList<Coffee> getAll() {

        ArrayList<Coffee> coffeeArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COFFEE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Coffee contact = new Coffee();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.set_coffee(cursor.getString(2));
                contact.set_foam(cursor.getString(3));
                contact.set_milk(cursor.getString(4));
                contact.set_liquid1(cursor.getString(5));
                contact.set_liquid2(cursor.getString(6));
                contact.set_type(Integer.parseInt(cursor.getString(7)));
                // Adding contact to list
                coffeeArrayList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return coffeeArrayList;

    }

    public int updateContact(Coffee coffee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, coffee.getName()); // Contact Name
        values.put(KEY_COFFEE, coffee.get_coffee()); // Contact Name
        values.put(KEY_FOAM, coffee.get_foam()); // Contact Name
        values.put(KEY_MILK, coffee.get_milk()); // Contact Name
        values.put(KEY_LIQUID1, coffee.get_liquid1()); // Contact Name
        values.put(KEY_LIQUID2, coffee.get_liquid2()); // Contact Name
        values.put(KEY_TYPE, coffee.get_type());

        // updating row
        int i = db.update(TABLE_COFFEE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(coffee.getId()) });
        db.close();
        return  i;
    }


    public Coffee getCoffee(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COFFEE, new String[] { KEY_NAME, KEY_FOAM, KEY_MILK, KEY_MILK, KEY_LIQUID1, KEY_LIQUID2, KEY_TYPE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Coffee contact = new Coffee(cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));
            // return contact
            contact.setId(id);
            db.close();
            return contact;
        }

        db.close();
        return null;
    }






}
