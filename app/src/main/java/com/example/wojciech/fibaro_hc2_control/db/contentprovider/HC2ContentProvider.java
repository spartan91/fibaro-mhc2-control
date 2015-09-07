package com.example.wojciech.fibaro_hc2_control.db.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.wojciech.fibaro_hc2_control.db.DatabaseHelper;
import com.example.wojciech.fibaro_hc2_control.db.models.File;
import com.example.wojciech.fibaro_hc2_control.db.models.FileList;
import com.example.wojciech.fibaro_hc2_control.db.models.User;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Wojciech on 2015-07-17.
 */
public class HC2ContentProvider extends ContentProvider {
    String TAG= HC2ContentProvider.class.getSimpleName();

    private DatabaseHelper database;

    private static final String AUTHORITY = "com.example.wojciech.fibaro_hc2_control.db.contentprovider;";
    //BASE PATHS
    private static final String BASE_PATH = "hc2";

    private static final String PATH_USER = /*BASE_PATH+*/User.TABLE_CC_USER;
    private static final String PATH_FILE = File.TABLE_CC_FILE;
    private static final String PATH_FILE_LIST = FileList.TABLE_CC_FILE_LIST;

    //CONTENT URI
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final Uri CONTENT_URI_USER = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_USER);
    public static final Uri CONTENT_URI_FILE = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_FILE);
    public static final Uri CONTENT_URI_FILE_LIST = Uri.parse("content://" + AUTHORITY
            + "/" + PATH_FILE_LIST);

    //CONTENT TYPE
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/safecloud";

    public static final String CONTENT_ITEM_TYPE_USER = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + CONTENT_URI_USER;
    public static final String CONTENT_ITEM_TYPE_FILE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + CONTENT_URI_FILE;
    public static final String CONTENT_ITEM_TYPE_FILE_LIST = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + CONTENT_URI_FILE_LIST;

    // URI Matcher

    private static final int USER = 10;
    private static final int USER_ID = 11;
    private static final int FILE = 20;
    private static final int FILE_ID = 21;
    private static final int FILE_LIST = 30;
    private static final int FILE_LIST_ID = 31;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, PATH_USER, USER);
        sURIMatcher.addURI(AUTHORITY, PATH_USER + "/#", USER_ID);
        sURIMatcher.addURI(AUTHORITY, PATH_FILE, FILE);
        sURIMatcher.addURI(AUTHORITY, PATH_FILE + "/#", FILE_ID);
        sURIMatcher.addURI(AUTHORITY, PATH_FILE_LIST, FILE_LIST);
        sURIMatcher.addURI(AUTHORITY, PATH_FILE_LIST + "/#", FILE_LIST_ID);
    }

    public boolean deleteDatabase=true;
    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");

        //if(deleteDatabase)
        //	DeleteDatabase();
        //boolean b= getContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);
        //Log.d(TAG, "onCreate: "+b);
        database = new DatabaseHelper(getContext());
        return false;
    }
    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        String id;

        switch (uriType) {
            case USER:
                rowsDeleted = sqlDB.delete(User.TABLE_CC_USER,
                        selection, selectionArgs);
                break;
            case USER_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(User.TABLE_CC_USER,
                            User.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(User.TABLE_CC_USER,
                            User.COLUMN_ID + "=" + id + " and "
                                    + selection, selectionArgs);
                }
                break;
            case FILE:
                rowsDeleted = sqlDB.delete(File.TABLE_CC_FILE,
                        selection, selectionArgs);
                break;
            case FILE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(File.TABLE_CC_FILE,
                            File.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(File.TABLE_CC_FILE,
                            File.COLUMN_ID + "=" + id + " and "
                                    + selection, selectionArgs);
                }
                break;

            case FILE_LIST:
                rowsDeleted = sqlDB.delete(FileList.TABLE_CC_FILE_LIST,
                        selection, selectionArgs);
                break;
            case FILE_LIST_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(FileList.TABLE_CC_FILE_LIST,
                            FileList.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(FileList.TABLE_CC_FILE_LIST,
                            FileList.COLUMN_ID + "=" + id + " and "
                                    + selection, selectionArgs);
                }
                break;



            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        //Log.d(TAG, "rowsDeleted " + rowsDeleted);
        return rowsDeleted;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //Log.d(TAG, "insert Uri uri");
        int uriType = sURIMatcher.match(uri);
        //Log.d(TAG, "insert uriType " + uriType);
        SQLiteDatabase sqlDB = database.getWritableDatabase();

        //Sprawdzenie tabel w bazie danych
		/*Cursor c = sqlDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		if (c.moveToFirst())
	    {
	        while ( !c.isAfterLast() ){
	        	Log.d(TAG, "TABLE " +c.getString( c.getColumnIndex("name")) );
	           c.moveToNext();
	        }
	    }*/

        //Log.d(TAG, "insert getWritableDatabase");
        long id = 0;

        switch (uriType) {
            case USER:
                id = sqlDB.insert(User.TABLE_CC_USER, null, values);
                break;
            case FILE:
                id = sqlDB.insert(File.TABLE_CC_FILE, null, values);
                break;
            case FILE_LIST:
                id = sqlDB.insert(FileList.TABLE_CC_FILE_LIST, null, values);
                break;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        // check if the caller has requested a column which does not exists
        checkColumns(projection, uriType);

        switch (uriType) {
            case USER:
                // Set the table
                queryBuilder.setTables(User.TABLE_CC_USER);
                break;
            case USER_ID:
                queryBuilder.setTables(User.TABLE_CC_USER);
                // adding the ID to the original query
                queryBuilder.appendWhere(User.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case FILE:
                // Set the table
                queryBuilder.setTables(File.TABLE_CC_FILE);
                break;
            case FILE_ID:
                queryBuilder.setTables(File.TABLE_CC_FILE);
                // adding the ID to the original query
                queryBuilder.appendWhere(File.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;

            case FILE_LIST:
                // Set the table
                queryBuilder.setTables(FileList.TABLE_CC_FILE_LIST);
                break;
            case FILE_LIST_ID:
                queryBuilder.setTables(FileList.TABLE_CC_FILE_LIST);
                // adding the ID to the original query
                queryBuilder.appendWhere(FileList.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();

        //Log.d(TAG, "queryBuilder2 db name:"+database.getDatabaseName());
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        //Log.d(TAG, "queryBuilder3: "+cursor.getCount());

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {

            case USER:
                rowsUpdated = sqlDB.update(User.TABLE_CC_USER,
                        values, selection, selectionArgs);
                break;
            case USER_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(User.TABLE_CC_USER,
                            values, User.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(User.TABLE_CC_USER,
                            values, User.COLUMN_ID + "=" + id + " and "
                                    + selection, selectionArgs);
                }
                break;

            case FILE:
                rowsUpdated = sqlDB.update(File.TABLE_CC_FILE,
                        values, selection, selectionArgs);
                break;
            case FILE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(File.TABLE_CC_FILE,
                            values, File.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(File.TABLE_CC_FILE,
                            values, File.COLUMN_ID + "=" + id + " and "
                                    + selection, selectionArgs);
                }
                break;

            case FILE_LIST:
                rowsUpdated = sqlDB.update(FileList.TABLE_CC_FILE_LIST,
                        values, selection, selectionArgs);
                break;
            case FILE_LIST_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(FileList.TABLE_CC_FILE_LIST,
                            values, FileList.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(FileList.TABLE_CC_FILE_LIST,
                            values, FileList.COLUMN_ID + "=" + id + " and "
                                    + selection, selectionArgs);
                }
                break;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection, int uriType) {
        String[] available;
        switch (uriType) {
            case USER:
                available = new String[] {
                        User.COLUMN_ID,
                        User.COLUMN_LOGIN,
                        User.COLUMN_EMAIL,
                        User.COLUMN_PASSWORD_HASH,
                        User.COLUMN_PIN_HASH,
                        User.COLUMN_KP,
                        User.COLUMN_PASSWORD_ENCRYPTED_BY_PIN,
                        User.COLUMN_PASSWORD_SALT,
                        User.COLUMN_PASSWORD_IV,
                        User.COLUMN_KD_ENCRYPTED_BY_PASSWORD,
                        User.COLUMN_KD_SALT,
                        User.COLUMN_KD_IV,
                        User.COLUMN_LAST_LOGED_AT,
                        User.COLUMN_IS_LOGED,
                        User.COLUMN_REGISTRED_AT};
                break;
            case FILE:
                available = new String[] {
                        File.COLUMN_ID,
                        File.COLUMN_NAME,
                        File.COLUMN_PATH,
                        File.COLUMN_ID_MOBILE_SEVICE,
                        File.COLUMN_CLOUD_ADDRESS,
                        File.COLUMN_DRIVE_ID,
                        File.COLUMN_MIME_TYPE,
                        File.COLUMN_HASH_RAW,
                        File.COLUMN_HASH_ENCRYPTED,
                        File.COLUMN_IS_PROCESSING,
                        File.COLUMN_IS_UPLOAD_REQUESTED,
                        File.COLUMN_IS_TRANSFERRING,
                        File.COLUMN_IS_ENCRYPTED_ON_DEVICE,
                        File.COLUMN_IS_ENCRYPTED_ON_DRIVE,
                        File.COLUMN_IS_ENCRYPTION_REQUESTED,
                        File.COLUMN_IS_DECRYPTION_REQUESTED,
                        File.COLUMN_IS_SHARED,
                        File.COLUMN_IS_SHARING_REQUESTED,
                        File.COLUMN_IS_ENCRYPTION_REQUESTED,
                        File.COLUMN_VERSION,
                        File.COLUMN_ENCRYPTION_FILE_TIME_START,
                        File.COLUMN_DECRYPTION_FILE_TIME_START,
                        File.COLUMN_ENCRYPTION_KEY_TIME_START,
                        File.COLUMN_DECRYPTION_KEY_TIME_START,
                        File.COLUMN_UPLOAD_FILE_TOTAL_TIME_START,
                        File.COLUMN_DOWNLOAD_FILE_TOTAL_TIME_START,
                        File.COLUMN_ENCRYPTION_FILE_TIME_END,
                        File.COLUMN_DECRYPTION_FILE_TIME_END,
                        File.COLUMN_ENCRYPTION_KEY_TIME_END,
                        File.COLUMN_DECRYPTION_KEY_TIME_END,
                        File.COLUMN_UPLOAD_FILE_TOTAL_TIME_END,
                        File.COLUMN_DOWNLOAD_FILE_TOTAL_TIME_END,
                        File.COLUMN_CREATED_AT,
                        File.COLUMN_LAST_MODIFIED };
                break;
            case FILE_LIST:
                available = new String[] {
                        FileList.COLUMN_ID,
                        FileList.COLUMN_ID_FILE,
                        FileList.COLUMN_ID_USER,
                        FileList.COLUMN_ID_MOBILE_SEVICE,
                        FileList.COLUMN_ID_DRIVE_PERMISSION,
                        FileList.COLUMN_ENCRYPTED_FILE_IV_BY_KP,
                        FileList.COLUMN_ENCRYPTED_FILE_KEY_BY_KP,
                        FileList.COLUMN_IS_OWNER,
                        FileList.COLUMN_OWNER_LOGIN,
                        FileList.COLUMN_SHARED_EMAIL};
                break;

            default:
                throw new IllegalArgumentException("Unknown URI type: " + uriType);
        }

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Illegal columns in projection!");
            }
        }
    }

    public boolean DeleteDatabase()
    {

        Log.d(TAG, "Deleting database...\n DB name: " + database.getDatabaseName());
        boolean b= getContext().deleteDatabase(database.getDatabaseName());

        Log.d(TAG, "deleteDatabase result: " + b);
        return b;
    }
}
