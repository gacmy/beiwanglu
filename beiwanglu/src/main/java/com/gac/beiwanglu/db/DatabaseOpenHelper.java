package com.gac.beiwanglu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/2.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="notes.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE=" INTERGER";


    private static final String COMMA_SEP=",";
    private static String SQL_CREATE_ENTRIES="CREATE TABLE "+ NoteContract.NoteEntry.TABLE_NAME+" ("+ NoteContract.NoteEntry._ID+" INTEGER PRIMARY KEY,"
            +  NoteContract.NoteEntry.COLUMN_NAME_COLOR+INTEGER_TYPE+COMMA_SEP+
            NoteContract.NoteEntry.COLUMN_NAME_CONTENT+TEXT_TYPE+COMMA_SEP+
            NoteContract.NoteEntry.COLUMN_NAME_DATE+TEXT_TYPE+COMMA_SEP+
            NoteContract.NoteEntry.COLUMN_NAME_LOCK+INTEGER_TYPE+COMMA_SEP+
            NoteContract.NoteEntry.COLUMN_NAME_NOTEID+TEXT_TYPE+COMMA_SEP+
            NoteContract.NoteEntry.COLUMN_NAME_PWD+TEXT_TYPE+")";

    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+ NoteContract.NoteEntry.TABLE_NAME;

    public DatabaseOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       onUpgrade(db,oldVersion,newVersion);
    }
}
