package com.example.debongz.nextldsaadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class OrderItem extends AppCompatActivity {
    TextView proNum, ProName, ProUnit, SaveOrder;
    EditText Qty;
    String url = "http://nextld.co.za/salesadmin/app/add_item.php";
    String urlb = "http://nextld.co.za/salesadmin/app/delete_item.php";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);

        Toolbar mytoolbar = (Toolbar) findViewById(R.id.toolbaritem);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setTitle("ADD ITEM");



        SharedPreferences ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
        final String order_id = ordershared.getString("order_id", "");
        String cust_nu = ordershared.getString("cust_number", "");
        String cust_na = ordershared.getString("cust_name", "");
        String cust_addr = ordershared.getString("cust_addr", "");

        proNum = (TextView)findViewById(R.id.item_number);
        ProName= (TextView)findViewById(R.id.item_name);
        ProUnit= (TextView)findViewById(R.id.item_unit);
        SaveOrder = (TextView)findViewById(R.id.save_order);
        Qty = (EditText)findViewById(R.id.input_qty);

        Intent in = getIntent();
        String item_number= in.getExtras().getString("item_number");
        String item_name= in.getExtras().getString("item_name");
        String item_unit= in.getExtras().getString("item_unit");
        String item_quantity= in.getExtras().getString("item_quantity");


        //string for the quantity
        String quantity = Qty.getText().toString();



        proNum.setText(item_number); // change to item_number
        ProName.setText(item_name);
        ProUnit.setText(item_unit);
        Qty.setText(item_quantity);






        SaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = getIntent();
                String seorderid = order_id;
                String seitemnum = in.getExtras().getString("item_number");
                String seitemname = in.getExtras().getString("item_name");
                String seitemunit = in.getExtras().getString("item_unit");
                String seitemquantity = Qty.getText().toString();




                if (seitemquantity.isEmpty()) {
                    Toast.makeText(OrderItem.this, "Quantity Value cannot be empty", Toast.LENGTH_SHORT).show();

                } else {

                    POSTITEM(seorderid, seitemnum, seitemname, seitemquantity, seitemunit);
                }
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
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
                String seitemnum = in.getExtras().getString("item_number");
                String seitemname = in.getExtras().getString("item_name");

                DELETEITEM(seorderid, seitemnum);

                break;
            case R.id.save:
                ordershared = getSharedPreferences("Order", Context.MODE_PRIVATE);
                order_id = ordershared.getString("order_id", "");
                in = getIntent();
                seorderid = order_id;
                seitemnum = in.getExtras().getString("item_number");
                seitemname = in.getExtras().getString("item_name");
                String seitemunit = in.getExtras().getString("item_unit");
                String seitemquantity = Qty.getText().toString();




                if (seitemquantity.isEmpty()) {
                    Toast.makeText(OrderItem.this, "Quantity Value cannot be empty", Toast.LENGTH_SHORT).show();

                } else {

                    POSTITEM(seorderid, seitemnum, seitemname, seitemquantity, seitemunit);
                }
                break;
            default:
                break;
        }
        return true;
    }


    public void goback(View view) {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
    }

    public void POSTITEM(final String seorderid, final String seitemnum, final String seitemname, final String seitemquantity, final String seitemunit) {

        Log.i("Hiteshurl", "" + url);
        RequestQueue requestQueuea = Volley.newRequestQueue(OrderItem.this);
        StringRequest stringRequesta = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String status = jsonObject1.getString("status");
                    String message = jsonObject1.getString("message");



                    if(status.equalsIgnoreCase("passed")) {

                       //

                        Toast.makeText(OrderItem.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderItem.this, Order.class);
                        startActivity(intent);
                        finish();
                    }
                    else{

                        Toast.makeText(OrderItem.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh",""+error);
                Toast.makeText(OrderItem.this, "Error ! Please check your network connection", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMapa = new HashMap<>();
                stringMapa.put("order_id",seorderid);
                stringMapa.put("item_number",seitemnum);
                stringMapa.put("item_name",seitemname);
                stringMapa.put("item_unit",seitemunit);
                stringMapa.put("item_quantity",seitemquantity);
                return stringMapa;
            }

        };

        requestQueuea.add(stringRequesta);
        //initialize the progress dialog and show it
        progressDialog = new ProgressDialog(OrderItem.this);
        progressDialog.setMessage("Saving ...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        progressDialog.setCancelable(true);

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

            public void onCancel(DialogInterface dialog) {
                Toast.makeText(OrderItem.this, "Saving in background", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(OrderItem.this, Order.class);
                startActivity(intent);

            }});

        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }});

    }
    public void DELETEITEM(final String seorderid, final String seitemnum) {

        Log.i("Hiteshurl", "" + urlb);
        RequestQueue requestQueuea = Volley.newRequestQueue(OrderItem.this);
        StringRequest stringRequesta = new StringRequest(Request.Method.POST, urlb, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String status = jsonObject1.getString("status");
                    String message = jsonObject1.getString("message");



                    if(status.equalsIgnoreCase("passed")) {

                        //

                        Toast.makeText(OrderItem.this, ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderItem.this, Order.class);
                        startActivity(intent);
                        finish();
                    }
                    else{

                        Toast.makeText(OrderItem.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh",""+error);
                Toast.makeText(OrderItem.this, "Error ! Please check your network connection", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMapa = new HashMap<>();
                stringMapa.put("order_id",seorderid);
                stringMapa.put("item_number",seitemnum);
                return stringMapa;
            }

        };

        requestQueuea.add(stringRequesta);

        //initialize the progress dialog and show it
        progressDialog = new ProgressDialog(OrderItem.this);
        progressDialog.setMessage("Deleting ...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

    }
}
