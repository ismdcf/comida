package com.hsenidmobile.comida;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by thamali on 11/14/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String Database_Name = "SQLiteDatabase.db";
    public static final String TABLE_NAME = " Employees";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String COLUMN_LAST_NAME = "SECOND_NAME";
    private static final int Database_Version = 1;
    private SQLiteDatabase database;

//    Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

    public SQLiteHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        /*database = this.getWritableDatabase();*/
        db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FIRST_NAME + " VARCHAR, " + COLUMN_LAST_NAME + " VARCHAR);");
        /*database.close();*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }

    public void insertRecord(ContactModel contact) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME, contact.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, contact.getLastName());
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public void updateRecord(ContactModel contact) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME, contact.getFirstName());
        contentValues.put(COLUMN_LAST_NAME, contact.getLastName());
        database.update(TABLE_NAME, contentValues, COLUMN_ID + "= ?", new String[]{contact.getID()});
        database.close();
    }


    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return 0;
    }

    public void deleteRecord(ContactModel contact) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, COLUMN_ID + "= ?", new String[]{contact.getID()});
        database.close();
    }

    public int delete(String table, String whereclause, String[] whereArgs){
        return 0;
    }

    public ArrayList<ContactModel> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<ContactModel> contacts = new ArrayList<ContactModel>();
        ContactModel contactModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                contactModel = new ContactModel();
                contactModel.setID(cursor.getString(0));
                contactModel.setFirstName(cursor.getString(1));
                contactModel.setLastName(cursor.getString(2));
                contacts.add(contactModel);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        database = this.getWritableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        database.close();
        return cursor;
    }

}



