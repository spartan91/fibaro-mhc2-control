package com.example.wojciech.fibaro_hc2_control.db.models;

/**
 * Created by Wojciech on 2015-02-05.
 */
public class File extends TableBase {
    public static final String TABLE_CC_FILE = "cc_file";

    public static final String COLUMN_ID = "id_file";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_CLOUD_ADDRESS = "cloud_address";
    public static final String COLUMN_ID_MOBILE_SEVICE = "id_mobile_service";
    public static final String COLUMN_MIME_TYPE = "mime_type";
    public static final String COLUMN_DRIVE_ID = "drive_id";
    public static final String COLUMN_HASH_RAW = "hash_raw";
    public static final String COLUMN_HASH_ENCRYPTED = "hash_encrypted";
    public static final String COLUMN_IS_ENCRYPTED_ON_DEVICE = "is_encrypted_on_device";
    public static final String COLUMN_IS_ENCRYPTED_ON_DRIVE = "is_encrypted_on_drive";
    public static final String COLUMN_IS_SHARED = "shared";
    public static final String COLUMN_IS_SHARING_REQUESTED = "sharing_requested";
    public static final String COLUMN_IS_ENCRYPTION_REQUESTED = "is_encryption_requested";
    public static final String COLUMN_IS_DECRYPTION_REQUESTED = "is_decryption_requested";
    public static final String COLUMN_IS_PROCESSING = "is_processing";
    public static final String COLUMN_IS_TRANSFERRING = "is_transferring";
    public static final String COLUMN_IS_UPLOAD_REQUESTED = "is_upload_requested";

    public static final String COLUMN_VERSION = "version";
    public static final String COLUMN_LAST_MODIFIED = "last_modified";
    public static final String COLUMN_CREATED_AT = "created_at";


    public static final String COLUMN_ENCRYPTION_FILE_TIME_START = "encryption_file_time_start";
    public static final String COLUMN_DECRYPTION_FILE_TIME_START = "decryption_file_time_start";
    public static final String COLUMN_ENCRYPTION_KEY_TIME_START = "encryption_key_time_start";
    public static final String COLUMN_DECRYPTION_KEY_TIME_START = "decryption_key_time_start";
    public static final String COLUMN_UPLOAD_FILE_TOTAL_TIME_START = "upload_file_total_time_start";
    public static final String COLUMN_DOWNLOAD_FILE_TOTAL_TIME_START = "download_file_total_time_start";

    public static final String COLUMN_ENCRYPTION_FILE_TIME_END = "encryption_file_time_end";
    public static final String COLUMN_DECRYPTION_FILE_TIME_END = "decryption_file_time_end";
    public static final String COLUMN_ENCRYPTION_KEY_TIME_END = "encryption_key_time_end";
    public static final String COLUMN_DECRYPTION_KEY_TIME_END = "decryption_key_time_end";
    public static final String COLUMN_UPLOAD_FILE_TOTAL_TIME_END = "upload_file_total_time_end";
    public static final String COLUMN_DOWNLOAD_FILE_TOTAL_TIME_END = "download_file_total_time_end";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_CC_FILE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PATH + " text, "
            + COLUMN_ID_MOBILE_SEVICE + " text, "
            + COLUMN_CLOUD_ADDRESS + " text, "
            + COLUMN_DRIVE_ID + " text, "
            + COLUMN_MIME_TYPE + " text not null, "
            + COLUMN_HASH_RAW + " text, "
            + COLUMN_HASH_ENCRYPTED + " text, "
            + COLUMN_IS_ENCRYPTED_ON_DEVICE + " integer not null, "
            + COLUMN_IS_ENCRYPTED_ON_DRIVE + " integer not null, "
            + COLUMN_VERSION + " integer, "
            + COLUMN_IS_SHARED + " integer not null, "
            + COLUMN_IS_SHARING_REQUESTED + " integer not null, "
            + COLUMN_IS_ENCRYPTION_REQUESTED + " integer not null, "
            + COLUMN_IS_DECRYPTION_REQUESTED + " integer not null, "
            + COLUMN_IS_PROCESSING + " integer not null, "
            + COLUMN_IS_TRANSFERRING + " integer not null, "
            + COLUMN_IS_UPLOAD_REQUESTED + " integer not null, "

            + COLUMN_ENCRYPTION_FILE_TIME_START + " integer, "
            + COLUMN_DECRYPTION_FILE_TIME_START + " integer, "
            + COLUMN_ENCRYPTION_KEY_TIME_START + " integer, "
            + COLUMN_DECRYPTION_KEY_TIME_START + " integer, "
            + COLUMN_UPLOAD_FILE_TOTAL_TIME_START + " integer, "
            + COLUMN_DOWNLOAD_FILE_TOTAL_TIME_START + " integer, "

            + COLUMN_ENCRYPTION_FILE_TIME_END + " integer, "
            + COLUMN_DECRYPTION_FILE_TIME_END + " integer, "
            + COLUMN_ENCRYPTION_KEY_TIME_END + " integer, "
            + COLUMN_DECRYPTION_KEY_TIME_END + " integer, "
            + COLUMN_UPLOAD_FILE_TOTAL_TIME_END + " integer, "
            + COLUMN_DOWNLOAD_FILE_TOTAL_TIME_END + " integer, "

            + COLUMN_LAST_MODIFIED + " integer not null, "
            + COLUMN_CREATED_AT + " integer not null "
            + ");";

}