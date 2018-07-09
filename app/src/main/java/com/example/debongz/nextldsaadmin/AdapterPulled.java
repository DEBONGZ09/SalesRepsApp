package com.example.debongz.nextldsaadmin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by DEBONGZ on 4/19/2018.
 */

public class AdapterPulled extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataOpen> data= Collections.emptyList();
    DataOpen current;
    int currentPos=0;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterPulled(Context context, List<DataOpen> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_quotes, parent,false);
        AdapterPulled.MyHolder holder=new AdapterPulled.MyHolder(view);
        return holder;
    }
    // Bind data
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        AdapterPulled.MyHolder myHolder= (AdapterPulled.MyHolder) holder;
        DataOpen current=data.get(position);
        myHolder.txtQnum.setText(current.order_quote_number);
        myHolder.txtQshop_name.setText(current.order_cust_name);
        myHolder.txtStatus.setText(current.order_status);
        myHolder.txtQdate.setText(current.order_date);

        String diff = current.order_diff;
        if (diff.equalsIgnoreCase("okay")){
            myHolder.txtQnum.setTextColor(Color.parseColor("#777777"));

        }
        else  if (diff.equalsIgnoreCase("mid")){
            myHolder.txtQnum.setTextColor(Color.parseColor("#f2602b"));

        }
        else  if (diff.equalsIgnoreCase("high")){
            myHolder.txtQnum.setTextColor(Color.parseColor("#d10000"));

        }


   }
    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder  {
        RelativeLayout quote_line;
        TextView txtQnum;
        TextView txtQshop_name;
        TextView txtQshop_area;
        TextView txtStatus;
        TextView txtQdate;
        //TextView txtQdate_required;




        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            quote_line=  (RelativeLayout)  itemView.findViewById(R.id.quote_line);
            txtQnum = (TextView) itemView.findViewById(R.id.txtqtotal);
            txtQshop_name = (TextView) itemView.findViewById(R.id.txtqname);
            txtStatus = (TextView) itemView.findViewById(R.id.txtqstatus);
            txtQdate = (TextView) itemView.findViewById(R.id.txtqdate);
            //txtQdate_required = (TextView) itemView.findViewById(R.id.likes_user_txt);


        }


    }

}
