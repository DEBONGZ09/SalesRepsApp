package com.example.debongz.nextldsaadmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.List;

/**
 * Created by DEBONGZ on 4/18/2018.
 */

public class PulledFragment extends Fragment {
    //defining views
    private RecyclerView RecycleViewChats;
    ProgressBar qloader;
    private RecyclerView comrecyclerView;
    private AdapterPulled mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_pulled, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
//getting views from xml
//        textViewToken = (TextView) getView().findViewById(R.id.textViewToken);
//        buttonDisplayToken = (Button) getView().findViewById(R.id.buttonDisplayToken);
        qloader = (ProgressBar)view.findViewById(R.id.OpenQoader);
        fetchQuotes();
    }
    private void fetchQuotes() {
        qloader.setVisibility(View.VISIBLE);
        //Toast.makeText(MomentsComments.this, "res: "+sepostid, Toast.LENGTH_SHORT).show();
        SharedPreferences shared = getContext().getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        String repname = shared.getString("user_name", "");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest sstringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_FETCH_PULLED+"?user="+repname,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        //Toast.makeText(getContext(), "res: "+result, Toast.LENGTH_SHORT).show();
                        qloader.setVisibility(View.GONE);
                        try {



                            JSONArray jArray = new JSONArray(result);

                            List<DataOpen> data = new ArrayList<>();


                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject d = jArray.getJSONObject(i);
                                JSONObject json_data = jArray.getJSONObject(i);
                                DataOpen proData = new DataOpen();

                                proData.order_id = json_data.getString("id");
                                proData.order_quote_number = json_data.getString("quote_number");
                                proData.order_cust_name = json_data.getString("customer_number_n_name");
                                proData.order_date = json_data.getString("date_of_sale");
                                proData.order_status = json_data.getString("status");
                                proData.order_diff = json_data.getString("dif");
                                data.add(proData);
                                //zmAdaptera.notifyDataSetChanged();
                            }

                            comrecyclerView = (RecyclerView) getView().findViewById(R.id.ReQuotes);
                            mAdapter = new AdapterPulled(getContext(),data);
                            comrecyclerView.setAdapter(mAdapter);
                            comrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(), "res: "+error, Toast.LENGTH_SHORT).show();
                        qloader.setVisibility(View.GONE);
                    }
                }) {

        };

        MyVolley.getInstance(getContext()).addToRequestQueue(sstringRequest);
        sstringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 1.0f));

    }

    @Override
    public void onResume() {
        fetchQuotes();
        super.onResume();
    }
}
