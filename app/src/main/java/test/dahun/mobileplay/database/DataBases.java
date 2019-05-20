package test.dahun.mobileplay.database;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String SONGID = "song_id";
        public static final String ISLIKE = "is_like";
        public static final String _TABLENAME0 = "song_is_like";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +SONGID+" integer not null , "
                +ISLIKE+" integer not null );";
    }
}
