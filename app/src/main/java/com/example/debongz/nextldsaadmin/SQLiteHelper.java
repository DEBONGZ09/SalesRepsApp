package com.example.debongz.nextldsaadmin;

/**
 * Created by DEBONGZ on 1/23/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="nextld";
    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Subject_Cnumber="cust_number";
    public static final String Table_Column_1_Subject_Cname="cust_name";
    public static final String Table_Column_1_Subject_Caddr="addr_line_one";

    public static final String TABLE_NAME="customers";

    public static final String Table_Column_1_Subject_Pid="mpid";
    public static final String Table_Column_1_Subject_Pname="mpname";
    public static final String Table_Column_1_Subject_Punit="mpunit";

    public static final String TABLE_P_NAME="inve";

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 4);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS customers (id INTEGER, cust_number TEXT, cust_name TEXT, addr_line_one TEXT)";
        database.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customers");
        onCreate(db);

    }

}