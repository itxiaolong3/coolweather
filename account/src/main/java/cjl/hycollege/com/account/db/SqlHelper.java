package cjl.hycollege.com.account.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xiaolong on 2017/6/21.
 */

public class SqlHelper extends SQLiteOpenHelper{
    public SqlHelper(Context context) {
        super(context, "account.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table info(_id integer primary key autoincrement,time varchar(20),money varchar(20),dec varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
