package com.example.debongz.nextldsaadmin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShopDetails extends AppCompatActivity {


    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mycustRecycleView;
    private AdapterCustomers mAdapter;
    ProgressBar loadcustbar;
    TextView search;
    DBController controller = new DBController(this);
    SearchView searchView = null;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> cust_number_Array;
    ArrayList<String> cust_NAME_Array;
    ArrayList<String> custAddr_Array;

    SQLiteHelper sqLiteHelper;
    Cursor cursor;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        loadcustbar = (ProgressBar)findViewById(R.id.load_re);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.shop_toolbar);
        search = (TextView)findViewById(R.id.serach_cust_txt);
        sqLiteHelper = new SQLiteHelper(this);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("Select Customer");



        cust_number_Array = new ArrayList<String>();

        cust_NAME_Array = new ArrayList<String>();

        custAddr_Array = new ArrayList<String>();


//
//        new AsyncFetch().execute();
        ShowSQLiteDBdata() ;


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Updating Array Adapter ListView after typing inside EditText.
                String e = search.getText().toString();



                //new AsyncFetch().execute();
                ShowSQLiteDBdata() ;


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ShowSQLiteDBdata() ;
                //new AsyncFetch().execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //new AsyncFetch().execute();
                ShowSQLiteDBdata() ;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mrefresh, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Intent intent = new Intent(ShopDetails.this, SaveProducts.class);
                startActivity(intent);

                break;

        }
        return true;
    }




    @Override
    public void onBackPressed()
    {
        //new AsyncFetch().cancel(true);
        Intent intent = new Intent(ShopDetails.this, Home.class);
        startActivity(intent);
        finish();

    }








    private void ShowSQLiteDBdata() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
       // List<DataCustomers> data=new ArrayList<>();

        String value = "%" + search.getText().toString() + "%";

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_NAME+" WHERE "+SQLiteHelper.Table_Column_1_Subject_Cname+" LIKE '"+value+"'  ", null);
        cust_number_Array.clear();
        cust_NAME_Array.clear();
        custAddr_Array.clear();

        if (cursor.moveToFirst()) {
            do {
                cust_number_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Subject_Cnumber)));
                cust_NAME_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Subject_Cname)));
     custAddr_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Subject_Caddr)));

//                        DataCustomers custData = new DataCustomers();
//                        data.clear();
//                        custData.CustName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Subject_Cname));
//                        custData.CustAddr = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Subject_Caddr));
//                        custData.ShopID  = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Subject_Cnumber));
//                        data.add(custData);



            }

            while (cursor.moveToNext());
        }

        mycustRecycleView = (RecyclerView) findViewById(R.id.list_view);
        mAdapter = new AdapterCustomers(ShopDetails.this, cust_number_Array,cust_NAME_Array,custAddr_Array);
        mycustRecycleView.setAdapter(mAdapter);
        mycustRecycleView.setLayoutManager(new LinearLayoutManager(ShopDetails.this));

        cursor.close();
    }

    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }
}



