package com.example.wojciech.fibaro_hc2_control.db.models;

/**
 * Created by Wojciech on 2015-02-05.
 */
public class User extends TableBase{
    public static final String TABLE_CC_USER = "cc_user";

    public static final String COLUMN_ID = "id_user";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD_HASH = "password_hash";
    public static final String COLUMN_PIN_HASH = "pin_hash";
    public static final String COLUMN_KP = "kp";
    public static final String COLUMN_PASSWORD_ENCRYPTED_BY_PIN = "password_encrypted";
    public static final String COLUMN_PASSWORD_SALT = "password_salt";
    public static final String COLUMN_PASSWORD_IV = "password_iv";
    public static final String COLUMN_KD_ENCRYPTED_BY_PASSWORD = "kd_encrypted";
    public static final String COLUMN_KD_SALT = "kd_salt";
    public static final String COLUMN_KD_IV = "kd_iv";
    public static final String COLUMN_LAST_LOGED_AT = "last_loged_at";
    public static final String COLUMN_IS_LOGED = "is_loged";
    public static final String COLUMN_REGISTRED_AT = "registred_at";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_CC_USER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LOGIN + " text not null, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_PASSWORD_HASH + " text not null, "
            + COLUMN_PIN_HASH + " text, "
            + COLUMN_KP + " text not null, "
            + COLUMN_PASSWORD_ENCRYPTED_BY_PIN + " text, "
            + COLUMN_PASSWORD_SALT + " text, "
            + COLUMN_PASSWORD_IV + " text, "
            + COLUMN_KD_ENCRYPTED_BY_PASSWORD + " text, "
            + COLUMN_KD_SALT + " text, "
            + COLUMN_KD_IV + " text, "
            + COLUMN_LAST_LOGED_AT + " integer, "
            + COLUMN_IS_LOGED + " integer not null, "
            + COLUMN_REGISTRED_AT + " integer not null "
            + ");";
}
