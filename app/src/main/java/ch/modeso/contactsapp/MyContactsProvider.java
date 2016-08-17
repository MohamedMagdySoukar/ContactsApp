package ch.modeso.contactsapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by User on 7/29/2016.
 */
public class MyContactsProvider extends ContentProvider {

    MyContactsDBHelper myDataBase;
    SQLiteDatabase db;

    // used for the UriMacher
    private static final int CONTACTS = 10;
    private static final int CONTACT_ID = 20;

    private static final String AUTHORITY = "ch.modeso.contactsapp.contentprovider";

    private static final String BASE_PATH = "contacts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher( UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, CONTACTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CONTACT_ID);
    }


    @Override
    public boolean onCreate() {
        myDataBase = new MyContactsDBHelper(getContext());
        db = myDataBase.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] mProjection, String mSelectionClause, String[] mSelectionArgs, String mSortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Constants.TABLE_CONTACTS);

        switch (sURIMatcher.match(uri)) {
            case CONTACTS:
                break;

            case CONTACT_ID:
                qb.appendWhere( Constants.COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (mSortOrder == null || mSortOrder == ""){
            /**
             * By default sort on contacts names
             */
            mSortOrder = Constants.COLUMN_NAME;
        }
        Cursor c = qb.query(db,	mProjection, mSelectionClause, mSelectionArgs,null, null, mSortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)){
            /**
             * Get all contacts records
             */
            case CONTACTS:
                return "vnd.android.cursor.dir/vnd.ch.modeso.contactsapp.contacts";

            /**
             * Get a particular contact
             */
            case CONTACT_ID:
                return "vnd.android.cursor.item/vnd.ch.modeso.contactsapp.contacts";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(Constants.TABLE_CONTACTS, "", contentValues);

        /**
         * If record is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
