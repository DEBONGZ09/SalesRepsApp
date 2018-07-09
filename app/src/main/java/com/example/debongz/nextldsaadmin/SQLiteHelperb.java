package com.example.debongz.nextldsaadmin;

/**
 * Created by DEBONGZ on 1/23/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelperb extends SQLiteOpenHelper {

    static String DATABASE_NAME="nextld";
    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Subject_Pid="id";
    public static final String Table_Column_1_Subject_Pname="pname";
    public static final String Table_Column_1_Subject_Punit="punit";

    public static final String TABLE_P_NAME="products";

    public SQLiteHelperb(Context context) {

        super(context, DATABASE_NAME, null, 4);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_P_TABLE="CREATE TABLE IF NOT EXISTS products (id TEXT, pname TEXT, punit TEXT)";
        database.execSQL(CREATE_P_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);

    }

}