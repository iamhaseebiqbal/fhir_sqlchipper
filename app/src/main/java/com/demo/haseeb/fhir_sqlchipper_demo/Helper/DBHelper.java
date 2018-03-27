package com.demo.haseeb.fhir_sqlchipper_demo.Helper;

import android.content.ContentValues;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    private static final String Pass_Phrase = "asdF!@#/";
    private static final int DB_VERSION = 1;
    private static final String DB_Name = "Fhir_Ikmb.db";

    private static final String Table_Name = "Patient";
    private static final String Column_Name = "last_name";

    private static final String SQL_Create_Table_Query =
            "CREATE TABLE " + Table_Name + " (" + Column_Name + " TEXT)";

    private static final String SQL_Drop_Table_Query =
            "DROP TABLE IF EXISTS " + Table_Name;


    public DBHelper(Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }

    static public synchronized DBHelper getInstance(Context context) {
        if(instance == null) {
            SQLiteDatabase.loadLibs(context);
            instance = new DBHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_Create_Table_Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_Drop_Table_Query);
        onCreate(db);
    }

    public void insertPatient(String lastName) {
        SQLiteDatabase db =  instance.getWritableDatabase(Pass_Phrase);

        ContentValues values = new ContentValues();
        values.put(Column_Name, lastName);

        db.insert(Table_Name, null, values);
        db.close();
    }

    public void deleteAllPatients() {
        SQLiteDatabase db =  instance.getWritableDatabase(Pass_Phrase);

        db.delete(Table_Name, null, null);
        db.close();
    }

    public List<String> getAllPatient(){
        SQLiteDatabase db =  instance.getWritableDatabase(Pass_Phrase);

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM '%s';", Table_Name), null);
        List<String> lastNames = new ArrayList<>();

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String last_name = cursor.getString(cursor.getColumnIndex(Column_Name));
                lastNames.add(last_name);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return lastNames;
    }

}
