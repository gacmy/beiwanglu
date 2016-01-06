package com.gac.beiwanglu.db;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/1/5.
 */
public class NoteContract {

    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME="t_note";
        public static final String COLUMN_NAME_DATE="date";
        public static final String COLUMN_NAME_LOCK="lock";
        public static final String COLUMN_NAME_COLOR="color";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_PWD = "pwd";
        public static final String COLUMN_NAME_NOTEID = "noteId";
    }
}
