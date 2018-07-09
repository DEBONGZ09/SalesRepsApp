package com.example.debongz.nextldsaadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/**
 * Created by DEBONGZ on 5/29/2018.
 */

public class HomeFragment extends Fragment {

    TextView reptxt;
    FloatingActionButton CreateOrder;
    ProgressBar loadiquotes;
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView list_q,zmycustRecycleViewa;
    private AdapterProducts mAdaptera;
    private AdapterQuotes zmAdaptera;
    RelativeLayout pop;
    SwipeRefreshLayout myrefresh;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_open, container, false);
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        reptxt = (TextView)getView().findViewById(R.id.rephometxt);
        CreateOrder = (FloatingActionButton) getView().findViewById(R.id.add_new_order);
        loadiquotes = (ProgressBar)getView().findViewById(R.id.load_quote);
        myrefresh = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout) ;

        CreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ShopDetails.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        SharedPreferences shared = getContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        String repname = shared.getString("user_name", "");

        String name = " Open orders for "+repname;

        reptxt.setText(name);

        new AsyncFetcha().execute();

        myrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                new AsyncFetcha().execute();
            }
        });

    }

    private class AsyncFetcha extends AsyncTask<String, String, String> {

        HttpURLConnection conn;
        URL urla = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            loadiquotes.setVisibility(View.INVISIBLE);
            myrefresh.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                urla = new URL("http://nextld.co.za/salesadmin/app/select_quotes.php");

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

                SharedPreferences shared = getContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                String repname = shared.getString("user_name", "");


                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("rep_name", repname);
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

            loadiquotes.setVisibility(View.INVISIBLE);
            myrefresh.setRefreshing(false);
            List<DataQuotes> data=new ArrayList<>();

            loadiquotes.setVisibility(View.INVISIBLE);
            myrefresh.setRefreshing(false);
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataQuotes proData = new DataQuotes();
                    proData.QcustName= json_data.getString("customer_number_n_name");
                    proData.QCustNum= "";
                    proData.QCustAddr= json_data.getString("customer_addr");
                    proData.QorderTotal= "R "+json_data.getString("order_total");
                    proData.QOrderDate= json_data.getString("date_of_sale");
                    proData.QStatus= json_data.getString("order_status");
                    proData.QOrderNum= json_data.getString("order_id");
                    data.add(proData);

//                    public String QcustName;
//                    public String QCustNum;
//                    public String QCustAddr;
//                    public String QorderTotal;
//                    public String QOrderDate;
//                    public String QStatus;
//                    public String QOrderNum;

                }

                // Setup and Handover data to recyclerview
                zmycustRecycleViewa = (RecyclerView) getView().findViewById(R.id.list_quotes);
                zmAdaptera = new AdapterQuotes(getContext(), data);
                zmycustRecycleViewa.setAdapter(zmAdaptera);
                zmycustRecycleViewa.setLayoutManager(new LinearLayoutManager(getContext()));

            } catch (JSONException e) {
//                Toast.makeText(Order.this, "add iems", Toast.LENGTH_LONG).show();
            }

        }

    }
}
