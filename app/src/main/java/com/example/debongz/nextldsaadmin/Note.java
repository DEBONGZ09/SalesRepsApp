package com.example.debongz.nextldsaadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Note extends AppCompatActivity {
    String url_save = "http://nextld.co.za/salesadmin/app/save_note.php";

    ProgressDialog progressDialog;
    EditText my_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.toolbaritem);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ADD NOTE");
        my_note =(EditText) findViewById(R.id.notetxt);
        Intent in = getIntent();
        String order_id= in.getExtras().getString("order_id");
        String note_text= in.getExtras().getString("note_text");

        if(note_text.equalsIgnoreCase("Click here to add note...")) {
            my_note.clearFocus();
        } else {
            my_note.setText(note_text);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                SharedPreferences ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
                String order_id = ordershared.getString("order_id", "");
                Intent in = getIntent();
                String seorderid = order_id;

                //DELETEITEM(seorderid, seitemnum);

                break;
            case R.id.save_note:
                SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                final String repname = shared.getString("user_name", "");

                ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
                order_id = ordershared.getString("order_id", "");
                String cust_nu = ordershared.getString("cust_number", "");
                String cust_na = ordershared.getString("cust_name", "");
                final String cust_addr = ordershared.getString("cust_addr", "");
                String note_txt = my_note.getText().toString();
                String senote = note_txt;
                final String full_shop_name = cust_nu + " " + cust_na;
                String full_addr = cust_addr;
                String full_repname = "Sales Person: " + repname;


                if (note_txt.isEmpty()) {
                    Toast.makeText(Note.this, "Note can not be empty", Toast.LENGTH_SHORT).show();

                } else {
                    seorderid = order_id;
                    String secustname = full_shop_name;
                    String secustaddr = cust_addr;
                    String sesalesrep = repname;
                    String seorder_note = note_txt;


                    SAVEQUOTE(seorderid, secustname, secustaddr, sesalesrep, seorder_note);
                }
                break;
            default:
                break;
        }
        return true;
    }
    public void SAVEQUOTE(final String seorderid, final String secustname, final String secustaddr, final String sesalesrep, final String seorder_note) {

        Log.i("Hiteshurl", "" + url_save);
        RequestQueue requestQueuea = Volley.newRequestQueue(Note.this);
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

                        Toast.makeText(Note.this, "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Note.this, Order.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(Note.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh", "" + error);
                Toast.makeText(Note.this, "Error ! Please check your network connection", Toast.LENGTH_SHORT).show();

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
                stringMapa.put("order_note", seorder_note);
                return stringMapa;
            }

        };

        requestQueuea.add(stringRequesta);
        //initialize the progress dialog and show it
        progressDialog = new ProgressDialog(Note.this);
        progressDialog.setMessage("Saving ...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        progressDialog.setCancelable(true);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

            public void onCancel(DialogInterface dialog) {
                Toast.makeText(Note.this, "Saving in background", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Note.this, Order.class);
                startActivity(intent);

            }});

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }});
        //ends of save request

    }
//    public void SAVENOTE(final String seorderid, final String senote) {
//
//        Log.i("Hiteshurl", "" + url_save);
//        RequestQueue requestQueuea = Volley.newRequestQueue(Note.this);
//        StringRequest stringRequesta = new StringRequest(Request.Method.POST, url_save, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                progressDialog.dismiss();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray("result");
//                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                    String status = jsonObject1.getString("status");
//                    String message = jsonObject1.getString("message");
//
//
//                    if (status.equalsIgnoreCase("passed")) {
//
//                        //
//
//                        Toast.makeText(Note.this, "" + message, Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Note.this, Order.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//
//                        Toast.makeText(Note.this, "" + message, Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Log.i("Hitesh", "" + error);
//                Toast.makeText(Note.this, "Error ! Please check your network connection", Toast.LENGTH_SHORT).show();
//
//            }
//        }) {
//
//            // seorderid, secustname, secustaddr, sesalesrep
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> stringMapa = new HashMap<>();
//                stringMapa.put("order_id", seorderid);
//                stringMapa.put("note_txt", senote);
//                return stringMapa;
//            }
//
//        };
//
//        requestQueuea.add(stringRequesta);
//        //initialize the progress dialog and show it
//        progressDialog = new ProgressDialog(Note.this);
//        progressDialog.setMessage("Saving ...");
////        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        progressDialog.setCancelable(true);
//
//        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
//
//            public void onCancel(DialogInterface dialog) {
//                Toast.makeText(Note.this, "Saving in background", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Note.this, Order.class);
//                startActivity(intent);
//
//            }});
//
//        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
//
//            public void onDismiss(DialogInterface dialog) {
//                // TODO Auto-generated method stub
//
//            }});
//        //ends of save request
//
//    }
}
