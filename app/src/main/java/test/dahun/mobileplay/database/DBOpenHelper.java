package test.dahun.mobileplay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DBOpenHelper(Context context){
        this.mCtx = context;
    }

    public DBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    // insert
    public long insertColumn(int song_id, int is_like){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.SONGID, song_id);
        values.put(DataBases.CreateDB.ISLIKE, is_like);
        Log.i("is_likeSQLTestinsert", "song_id : "+song_id);
        Log.i("is_likeSQLTestinsert", "is like : "+is_like);
        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }

    // select
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    // update
    public boolean updateColumn(int song_id, int is_like){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.SONGID, song_id);
        values.put(DataBases.CreateDB.ISLIKE, is_like);
        return mDB.update(DataBases.CreateDB._TABLENAME0, values, "song_id=" + song_id, null) > 0;
    }

    // delete all
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }
}