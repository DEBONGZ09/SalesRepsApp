package com.example.debongz.nextldsaadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class SaveDataBase extends AppCompatActivity {
    TextView protxt;
    ProgressBar progressBar;
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
    Integer count =1;

    SQLiteHelper sqLiteHelper;
    Cursor cursor;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data_base);
        protxt = (TextView)findViewById(R.id.progresstxt);
        progressBar = (ProgressBar)findViewById(R.id.simpleProgressBar);
        sqLiteHelper = new SQLiteHelper(this);
        progressBar.setMax(10);
        protxt.setText("Loading Customers...");
        SQLiteDataBaseBuild();

        SQLiteTableBuild();

        DeletePreviousData();
        new AsyncFetchCustomers().execute();


    }


    private class AsyncFetchCustomers extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            //loadcustbar.setVisibility(View.VISIBLE);

            count =1;
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(10);
            protxt.setText("Loading Customers...");



        }

        @Override
        protected String doInBackground(String... params) {



            try {

                // Enter URL address where your php file resides
                url = new URL("http://nextld.co.za/salesadmin/app/cust_search.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url

                String searchQuery = "";
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }


        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            progressBar.setVisibility(View.GONE);
            List<DataCustomers> data=new ArrayList<>();


            //Toast.makeText(SaveDataBase.this,"Load Done", Toast.LENGTH_LONG).show();
            protxt.setText("Loaded successfully");
            if(result.equals("no rows")) {progressBar.setVisibility(View.GONE);
                //Toast.makeText(ShopDetails.this, "The Customer was not found in the database", Toast.LENGTH_LONG).show();
            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
//                        DataCustomers custData = new DataCustomers();
//                        custData.CustName = json_data.getString("cust_name");
//                        custData.CustAddr = json_data.getString("addr_line_one");
//                        custData.ShopID  = json_data.getString("cust_number");
//                        data.add(custData);

                        String cid = json_data.getString("id");
                        String cnumber = json_data.getString("cust_number");
                        String cname = json_data.getString("cust_name");
                        String caddr = json_data.getString("addr_line_one");


                        String SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (id,cust_number,cust_name,addr_line_one) VALUES('"+cid+"', '"+cnumber+"', '"+cname+"', '"+caddr+"');";

                        sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                        SharedPreferences shared = getSharedPreferences("SetupPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("loaded", "yebo");
                        editor.commit();

                        Intent intent = new Intent(SaveDataBase.this, Home.class);
                        startActivity(intent);
                        finish();
                    }


//                    // Setup and Handover data to recyclerview
//                    mycustRecycleView = (RecyclerView) findViewById(R.id.list_view);
//                    mAdapter = new AdapterCustomers(ShopDetails.this, data);
//                    mycustRecycleView.setAdapter(mAdapter);
//                    mycustRecycleView.setLayoutManager(new LinearLayoutManager(ShopDetails.this));

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
//                    Toast.makeText(ShopDetails.this, e.toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(ShopDetails.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }
    }
    public void SQLiteDataBaseBuild(){

        sqLiteDatabase = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild(){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS customers (id INTEGER, cust_number TEXT, cust_name TEXT, addr_line_one TEXT)");
        //sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS products (id INTEGER, cust_number TEXT, cust_name TEXT, addr_line_one TEXT)");

    }

    public void DeletePreviousData(){

        sqLiteDatabase.execSQL("DELETE FROM "+SQLiteHelper.TABLE_NAME+"");

    }


}