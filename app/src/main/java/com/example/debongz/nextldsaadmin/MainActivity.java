package com.example.debongz.nextldsaadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    String url = "http://nextld.co.za/salesadmin/app/signin.php";
    String pass, user_name;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String seuser_name = username.getText().toString();
               String sepass = password.getText().toString();
                if (seuser_name.isEmpty() || sepass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "fill details", Toast.LENGTH_SHORT).show();

                } else {

                    login(seuser_name, sepass);
                }
            }
        });
        

    }
    public void login(final String seuser_name, final String sepass) {

        Log.i("Hiteshurl", "" + url);
        RequestQueue requestQueueone = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequestone = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String status = jsonObject1.getString("status");
                    String user_type = jsonObject1.getString("user_type");
                    String user_full_name = jsonObject1.getString("user_full_name");
                    String user_name = jsonObject1.getString("user_name");
                    String user_email = jsonObject1.getString("user_email");
                    String password = jsonObject1.getString("password");
                    String message = jsonObject1.getString("message");



                    if(status.equalsIgnoreCase("passed")) {

                        SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("pass", password);
                        editor.putString("user_name", user_name);
                        editor.putString("user_type", user_type);
                        editor.putString("user_full_name", user_full_name);
                        editor.putString("user_email", user_email);
                        editor.commit();
                        SharedPreferences mshared = getSharedPreferences("SetupPref", Context.MODE_PRIVATE);
                        String loaded = mshared.getString("loaded", "");
                        if (loaded.length() == 0) {

                            Intent intent = new Intent(MainActivity.this, SaveDataBase.class);
                            startActivity(intent);
                            finish();


                        }
                        else {

                            Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else{

                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("Hitesh",""+error);
                Toast.makeText(MainActivity.this, "Error signing in ! Please check your network connection", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put("username",seuser_name);
                stringMap.put("password",sepass);

                return stringMap;
            }

        };

        requestQueueone.add(stringRequestone);
        //initialize the progress dialog and show it
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

    }
    @Override
    public void onBackPressed()
    {

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);


    }


}
