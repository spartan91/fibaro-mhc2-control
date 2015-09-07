package com.example.wojciech.fibaro_hc2_control.db.models;

/**
 * Created by Wojciech on 2015-02-05.
 */
public class FileList extends TableBase{
    public static final String TABLE_CC_FILE_LIST = "cc_file_list";

    public static final String COLUMN_ID = "id_file_list";
    public static final String COLUMN_ID_USER = User.COLUMN_ID;
    public static final String COLUMN_ID_FILE = File.COLUMN_ID;
    public static final String COLUMN_IS_OWNER = "is_owner";
    public static final String COLUMN_ID_MOBILE_SEVICE = "id_mobile_service";
    public static final String COLUMN_ID_DRIVE_PERMISSION = "id_drive_permission";
    public static final String COLUMN_OWNER_LOGIN = "owner_login";
    public static final String COLUMN_SHARED_EMAIL = "shared_email";
    public static final String COLUMN_ENCRYPTED_FILE_KEY_BY_KP = "encrypted_file_key_by_kp";
    public static final String COLUMN_ENCRYPTED_FILE_IV_BY_KP = "encrypted_file_iv_by_kp";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_CC_FILE_LIST
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ID_USER + " text not null, "
            + COLUMN_ID_FILE + " integer not null, "
            + COLUMN_IS_OWNER + " integer not null, "
            + COLUMN_ID_MOBILE_SEVICE + " text, "
            + COLUMN_ID_DRIVE_PERMISSION + " text, "
            + COLUMN_OWNER_LOGIN + " text not null, "
            + COLUMN_SHARED_EMAIL + " text, "
            + COLUMN_ENCRYPTED_FILE_KEY_BY_KP + " text, "
            + COLUMN_ENCRYPTED_FILE_IV_BY_KP + " text, "
            + "FOREIGN KEY ("+COLUMN_ID_USER+") REFERENCES "+User.TABLE_CC_USER+"("+COLUMN_ID_USER+") "
            + "FOREIGN KEY ("+COLUMN_ID_FILE+") REFERENCES "+File.TABLE_CC_FILE+"("+COLUMN_ID_FILE+") "
            + ");";

}