package com.example.debongz.nextldsaadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.debongz.nextldsaadmin.Order.CONNECTION_TIMEOUT;
import static com.example.debongz.nextldsaadmin.Order.READ_TIMEOUT;

public class Order extends AppCompatActivity {
    TextView cust_name,shop_addr,sales_person,save_quote;
    ProgressBar loadprobar,loaditem;
    LinearLayout notdiv;
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mycustRecycleViewa,zmycustRecycleViewa;
    private AdapterProducts mAdaptera;
    private AdapterOrder zmAdaptera;
    RelativeLayout pop;
    EditText editText;
    TextView order_note;
    String url_save = "http://nextld.co.za/salesadmin/app/save_order.php";
    String url_note = "http://nextld.co.za/salesadmin/app/select_note.php";

    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> p_name_Array;
    ArrayList<String> p_id_NAME_Array;
    ArrayList<String> p_unit_Array;

    SQLiteHelperb sqLiteHelperb;
    Cursor cursor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        sqLiteHelperb = new SQLiteHelperb(this);
        //Make call to AsyncTask
        p_name_Array = new ArrayList<String>();

        p_id_NAME_Array = new ArrayList<String>();

        p_unit_Array = new ArrayList<String>();

notdiv =(LinearLayout) findViewById(R.id.note) ;
        order_note =(TextView)findViewById(R.id.thenotetxtx);
        editText = (EditText) findViewById(R.id.serach_txt);
        cust_name = (TextView) findViewById(R.id.shop_name);
        shop_addr = (TextView) findViewById(R.id.shop_addr);
        sales_person = (TextView) findViewById(R.id.sales_person);
        loadprobar = (ProgressBar) findViewById(R.id.load_re_items);
        loaditem = (ProgressBar) findViewById(R.id.load_order);
        save_quote = (TextView) findViewById(R.id.save_quote);

        SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        final String repname = shared.getString("user_name", "");

        SharedPreferences ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
        final String order_id = ordershared.getString("order_id", "");
        String cust_nu = ordershared.getString("cust_number", "");
        String cust_na = ordershared.getString("cust_name", "");
        final String cust_addr = ordershared.getString("cust_addr", "");

        String seorderid = order_id;
        show_note(seorderid);

        final String full_shop_name = cust_nu + " " + cust_na;
        String full_addr = cust_addr;
        String full_repname = "Sales Person: " + repname;

        cust_name.setText(full_shop_name);
        shop_addr.setText(full_addr);
        sales_person.setText(full_repname);
        ShowSQLiteDBdata() ;
        //new AsyncFetchb().execute();
        new AsyncFetcha().execute();
        //new AsyncFetch().execute();

        save_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seorderid = order_id;
                String secustname = full_shop_name;
                String secustaddr = cust_addr;
                String sesalesrep = repname;


                SAVEQUOTE(seorderid, secustname, secustaddr, sesalesrep);

            }
        });
        // Calling EditText addTextChangedListener method which controls the EditText type sequence.
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Updating Array Adapter ListView after typing inside EditText.
                String e = editText.getText().toString();
                ShowSQLiteDBdata() ;


                    //new AsyncFetchSearch().execute();



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                ShowSQLiteDBdata() ;
                //new AsyncFetchSearch().execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //new AsyncFetchSearch().execute();

            }
        });
    }


    public void hidePop(View view) {
        pop = (RelativeLayout)findViewById(R.id.add_items_pop_up);
        pop.setVisibility(View.INVISIBLE);

    }

    public void showPop(View view) {
        pop = (RelativeLayout)findViewById(R.id.add_items_pop_up);
        pop.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBackPressed()
    {
        SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        final String repname = shared.getString("user_name", "");
        SharedPreferences ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
        final String order_id = ordershared.getString("order_id", "");
        String cust_nu = ordershared.getString("cust_number", "");
        String cust_na = ordershared.getString("cust_name", "");
        final String cust_addr = ordershared.getString("cust_addr", "");


        final String full_shop_name = cust_nu + " " + cust_na;
        String full_addr = cust_addr;
        String full_repname = "Sales Person: " + repname;
        String seorderid = order_id;
        String secustname = full_shop_name;
        String secustaddr = cust_addr;
        String sesalesrep = repname;


        SAVEQUOTE(seorderid, secustname, secustaddr, sesalesrep);
        Intent intent = new Intent(Order.this, Home.class);
        startActivity(intent);
    }

    public void goback(View view) {
        SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        final String repname = shared.getString("user_name", "");
        SharedPreferences ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
        final String order_id = ordershared.getString("order_id", "");
        String cust_nu = ordershared.getString("cust_number", "");
        String cust_na = ordershared.getString("cust_name", "");
        final String cust_addr = ordershared.getString("cust_addr", "");


        final String full_shop_name = cust_nu + " " + cust_na;
        String full_addr = cust_addr;
        String full_repname = "Sales Person: " + repname;
        String seorderid = order_id;
        String secustname = full_shop_name;
        String secustaddr = cust_addr;
        String sesalesrep = repname;


        SAVEQUOTE(seorderid, secustname, secustaddr, sesalesrep);
        Intent intent = new Intent(Order.this, Home.class);
        startActivity(intent);
    }

    public void addNote(View view) {
        SharedPreferences ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
        final String order_id = ordershared.getString("order_id", "");
        String order_note_txt = order_note.getText().toString();

        Intent intent = new Intent(Order.this, Note.class);
        intent.putExtra("order_id",order_id );
        intent.putExtra("note_text", order_note_txt);
        startActivity(intent);
    }


    //async 2

    private class AsyncFetcha extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL urla = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            loaditem.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                urla = new URL("http://nextld.co.za/salesadmin/app/select_items.php");

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

                SharedPreferences ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
                String order_id = ordershared.getString("order_id", "");


                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("order_number", order_id);
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

            //this method will be running on UI thread

            loaditem.setVisibility(View.INVISIBLE);
            List<DataOrder> data=new ArrayList<>();

            loaditem.setVisibility(View.INVISIBLE);
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataOrder proData = new DataOrder();
                    proData.OderName= json_data.getString("item_name");
                    proData.OrderUnit= json_data.getString("item_unit");
                    proData.OrderQty= json_data.getString("item_quantity");
                    proData.OrderTotal= "R "+json_data.getString("item_total");
                    proData.OrderNum= json_data.getString("item_number");
                    data.add(proData);

//


                }

                // Setup and Handover data to recyclerview
                zmycustRecycleViewa = (RecyclerView) findViewById(R.id.list_order);
                zmAdaptera = new AdapterOrder(Order.this, data);
                zmycustRecycleViewa.setAdapter(zmAdaptera);
                zmycustRecycleViewa.setLayoutManager(new LinearLayoutManager(Order.this));

            } catch (JSONException e) {
//                Toast.makeText(Order.this, "add iems", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void ShowSQLiteDBdata() {

        sqLiteDatabase = sqLiteHelperb.getWritableDatabase();
        // List<DataCustomers> data=new ArrayList<>();

        String value = "%" + editText.getText().toString() + "%";

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+SQLiteHelperb.TABLE_P_NAME+" WHERE "+SQLiteHelperb.Table_Column_1_Subject_Pname+" LIKE '"+value+"'  ", null);
        p_name_Array.clear();
        p_id_NAME_Array.clear();
        p_unit_Array.clear();

        if (cursor.moveToFirst()) {
            do {

                p_id_NAME_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelperb.Table_Column_1_Subject_Pid)));

                p_unit_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelperb.Table_Column_1_Subject_Punit)));

                p_name_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelperb.Table_Column_1_Subject_Pname)));



            }

            while (cursor.moveToNext());
        }

        // Setup and Handover data to recyclerview
        mycustRecycleViewa = (RecyclerView) findViewById(R.id.product_list);
        mAdaptera = new AdapterProducts(Order.this, p_id_NAME_Array,p_name_Array,p_unit_Array);
        mycustRecycleViewa.setAdapter(mAdaptera);
        mycustRecycleViewa.setLayoutManager(new LinearLayoutManager(Order.this));


        cursor.close();
    }
    

    public void SAVEQUOTE(final String seorderid, final String secustname, final String secustaddr, final String sesalesrep) {

        Log.i("Hiteshurl", "" + url_save);
        RequestQueue requestQueuea = Volley.newRequestQueue(Order.this);
        StringRequest stringRequesta = new StringRequest(Request.Method.POST, url_save, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String status = jsonObject1.getString("status");
                    String message = jsonObject1.getString("message");


                    if (status.equalsIgnoreCase("passed")) {

                        //

                        Toast.makeText(Order.this, "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Order.this, Home.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(Order.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh", "" + error);
                Toast.makeText(Order.this, "Error ! Please check your network connection", Toast.LENGTH_SHORT).show();

            }
        }) {

            // seorderid, secustname, secustaddr, sesalesrep
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMapa = new HashMap<>();
                stringMapa.put("order_id", seorderid);
                stringMapa.put("cust_name", secustname);
                stringMapa.put("cust_addr", secustaddr);
                stringMapa.put("sales_rep", sesalesrep);
                return stringMapa;
            }

        };

        requestQueuea.add(stringRequesta);
        //initialize the progress dialog and show it
        progressDialog = new ProgressDialog(Order.this);
        progressDialog.setMessage("Saving ...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        progressDialog.setCancelable(true);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

            public void onCancel(DialogInterface dialog) {
                Toast.makeText(Order.this, "Saving in background", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Order.this, Home.class);
                startActivity(intent);

            }});

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }});
        //ends of save request

    }
    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }

    public void show_note(final String seorderid) {

        Log.i("Hiteshurl", "" + url_note);
        RequestQueue requestQueueone = Volley.newRequestQueue(Order.this);
        StringRequest stringRequestone = new StringRequest(Request.Method.POST, url_note, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String myorder_note = jsonObject1.getString("order_note");
                    String seva = jsonObject1.getString("seva");

                    if(myorder_note.isEmpty() || myorder_note.equalsIgnoreCase("null")){
                        notdiv.setVisibility(View.VISIBLE);
                        order_note.setVisibility(View.VISIBLE);
                        order_note.setText("Click here to add note...");
                    }
                    else if (seva.equalsIgnoreCase("cha")){
                        notdiv.setVisibility(View.GONE);
                        order_note.setVisibility(View.GONE);
                    }
                    else {
                        notdiv.setVisibility(View.VISIBLE);
                        order_note.setVisibility(View.VISIBLE);
                        order_note.setText(myorder_note);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Log.i("Hitesh",""+error);
//                Toast.makeText(MainActivity.this, "Error signing in ! Please check your network connection", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put("order_id",seorderid);
                return stringMap;
            }

        };

        requestQueueone.add(stringRequestone);
        //initialize the progress dialog and show it


    }

}
