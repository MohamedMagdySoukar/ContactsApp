package ch.modeso.contactsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 7/29/2016.
 */
public class MyContactsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + Constants.TABLE_CONTACTS
            + "("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_NAME + " text not null, "
            + Constants.COLUMN_PHONE + " text not null"
            + ");";

    public MyContactsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w(MyContactsDBHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACTS);
        onCreate(sqLiteDatabase);
    }
}
