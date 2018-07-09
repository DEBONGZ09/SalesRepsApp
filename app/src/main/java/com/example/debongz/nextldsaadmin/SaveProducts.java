package com.example.debongz.nextldsaadmin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import android.database.sqlite.SQLiteStatement;

public class SaveProducts extends AppCompatActivity {
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

    SQLiteHelperb sqLiteHelperb;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_products);
        protxt = (TextView)findViewById(R.id.progresstxt);
        progressBar = (ProgressBar)findViewById(R.id.simpleProgressBar);
        sqLiteHelperb = new SQLiteHelperb(this);
        progressBar.setMax(10);
        protxt.setText("Loading Products...");
        SQLiteDataBaseBuild();
        SQLiteTableBuild();

        DeletePreviousData();
        new AsyncFetchSearch().execute();
    }


    public class AsyncFetchSearch extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL urla = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            count =1;
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(10);
            protxt.setText("Loading Products...");

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                urla = new URL("http://nextld.co.za/salesadmin/app/select_products.php");



            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) urla.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);
                String search_text = "";



                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("product_name", search_text);
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

                    return ("unsuccessful");
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


            List<DataProducts> data=new ArrayList<>();


            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    String pname = json_data.getString("strDesc");
                    String punit = json_data.getString("strSellUnit");
                    String pid = json_data.getString("strPartNumber");



                    String mpname = pname.replaceAll("'", "");
                    //pname = pname.replace("'","\'");

                    String SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelperb.TABLE_P_NAME+" (id,pname,punit) VALUES(?1,?2,?3);";
                    SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
                    sqLiteDatabase.beginTransaction();

                        statement.clearBindings();
                        statement.bindString(1, pid);
                        statement.bindString(2, pname);
                        statement.bindString(3, punit);
                        statement.execute();

                    sqLiteDatabase.setTransactionSuccessful();
                    sqLiteDatabase.endTransaction();
                    //sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                    progressBar.setVisibility(View.GONE);
                    protxt.setText("Loaded successfully");
                    Intent intent = new Intent(SaveProducts.this, Home.class);
                    startActivity(intent);
                    finish();

                }



            } catch (JSONException e) {
                //Toast.makeText(Order.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }


    }
    public void SQLiteDataBaseBuild(){

        sqLiteDatabase = openOrCreateDatabase(SQLiteHelperb.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild(){

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS products (id TEXT, pname TEXT, punit TEXT)");

    }

    public void DeletePreviousData(){

        sqLiteDatabase.execSQL("DELETE FROM "+SQLiteHelperb.TABLE_P_NAME+"");

    }
}
