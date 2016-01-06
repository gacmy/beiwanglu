package com.gac.beiwanglu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gac.beiwanglu.bean.Notes;
import com.gac.beiwanglu.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/2.
 */
public class DbOp {
    private DatabaseOpenHelper mDbHelper;
    public DbOp(Context context){
        mDbHelper = new DatabaseOpenHelper(context);
    }


    /*
    *    NoteContract.NoteEntry.COLUMN_NAME_COLOR
         NoteContract.NoteEntry.COLUMN_NAME_CONTENT
         NoteContract.NoteEntry.COLUMN_NAME_DATE
         NoteContract.NoteEntry.COLUMN_NAME_NOTEID
         NoteContract.NoteEntry.COLUMN_NAME_LOCK
         NoteContract.NoteEntry.COLUMN_NAME_PWD
    * */
    public long insert(Notes note){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NAME_COLOR,note.color);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_CONTENT,note.content);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_DATE,note.date);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_NOTEID,note.ids);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_LOCK,note.lock);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_PWD, note.pwd);

        //插入新的行 返回新行的主键值
        long newRowId;
        newRowId = db.insert(NoteContract.NoteEntry.TABLE_NAME, "null",values);
        Utils.log("rowId:"+newRowId);
        db.close();
        return newRowId;
    }


    /*
    *
    *
    *   String[] projection = {
                UserContract.UserEntry.COLUMN_NAME_USERNAME,
                UserContract.UserEntry.COLUMN_NAME_USERAGE,
                UserContract.UserEntry.COLUMN_NAME_SEX
        };//返回结果的列

        //String whereClause = "column1 = ? OR column1 = ?";
        //查询语句
        String selection =
                UserContract.UserEntry.COLUMN_NAME_USERAGE+"=?";

        String[] selectionArgs={"11"};//where查询语句匹配的值
        String sortOrder = UserContract.UserEntry.COLUMN_NAME_USERNAME +" DESC";//按照什么排序
    * */
    //使用反射 获得字段名 填充类
    public ArrayList<Notes> query(){
        String[] projection={NoteContract.NoteEntry.COLUMN_NAME_COLOR,
                NoteContract.NoteEntry.COLUMN_NAME_CONTENT,
                NoteContract.NoteEntry.COLUMN_NAME_DATE,
                NoteContract.NoteEntry.COLUMN_NAME_NOTEID,
                NoteContract.NoteEntry.COLUMN_NAME_LOCK,
                NoteContract.NoteEntry.COLUMN_NAME_PWD};
        String selection=null;
        String[] selectionArgs=null;
        String sortOrder = NoteContract.NoteEntry.COLUMN_NAME_DATE +" DESC";//按照什么排序
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ArrayList<Notes> list = new ArrayList<Notes>();
        Cursor c = db.query(
               NoteContract.NoteEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
       // boolean lock = db.isDbLockedByCurrentThread();
        c.moveToFirst();
        Utils.log("query:" + c.isAfterLast());
       // Utils.log("lock:"+lock);
        while (!c.isAfterLast()) {
            Notes note = new Notes();
            note.color = c.getInt(c.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_COLOR));
            note.content = c.getString(c.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_CONTENT));
            note.date = c.getString(c.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_DATE));
            note.ids = c.getString(c.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_NOTEID));
            note.lock = c.getInt(c.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_LOCK));
            note.pwd = c.getString(c.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_PWD));
            list.add(note);
            c.moveToNext();
        }
        c.close();
        db.close();

        return list;
    }

    public void delete(Notes note){
        // Define 'where' part of query.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = NoteContract.NoteEntry.COLUMN_NAME_NOTEID + "= ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = { note.ids };
// Issue SQL statement.
        db.delete(NoteContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public void updatePwd(Notes note){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NoteContract.NoteEntry.COLUMN_NAME_LOCK,note.lock);

// Which row to update, based on the ID
        String selection = NoteContract.NoteEntry.COLUMN_NAME_NOTEID + " = ?";
        String[] selectionArgs = { note.ids };


        int count = db.update(
                NoteContract.NoteEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }
    public void update(Notes note){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NAME_COLOR,note.color);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_CONTENT,note.content);
        values.put(NoteContract.NoteEntry.COLUMN_NAME_LOCK,note.lock);

// Which row to update, based on the ID
        String selection = NoteContract.NoteEntry.COLUMN_NAME_NOTEID + " = ?";
        String[] selectionArgs = { note.ids };


        int count = db.update(
                NoteContract.NoteEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }
}
