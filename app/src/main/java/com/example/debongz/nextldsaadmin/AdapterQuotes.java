package com.example.debongz.nextldsaadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by DEBONGZ on 1/6/2018.
 */

public class AdapterQuotes extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataQuotes> data= Collections.emptyList();
    DataQuotes current;
    int currentPos=0;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterQuotes(Context context, List<DataQuotes> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_quotes, parent,false);
        AdapterQuotes.MyHolder holder=new AdapterQuotes.MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        AdapterQuotes.MyHolder myHolder= (AdapterQuotes.MyHolder) holder;
        DataQuotes current=data.get(position);
        myHolder.txtQcustName.setText(current.QcustName);
        myHolder.txtQCustNum.setText(current.QCustNum);
        myHolder.txtQorderTotal.setText(current.QorderTotal);
        myHolder.txtQOrderDate.setText(current.QOrderDate);
        myHolder.txtQStatus.setText(current.QStatus);
        myHolder.txtQOrderNum.setText(current.QOrderNum);
        myHolder.txtQCustAddr.setText(current.QCustAddr);

//        public String QcustName;
//        public String QCustNum;
//        public String QorderTotal;
//        public String QOrderDate;
//        public String QStatus;
//        public String QOrderNum;

    }
    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtQcustName;
        TextView txtQCustNum;
        TextView txtQorderTotal;
        TextView txtQOrderDate;
        TextView txtQStatus;
        TextView txtQOrderNum;
        TextView txtQCustAddr;



        // create constructor to get widget reference
        public MyHolder(View itemViewa) {
            super(itemViewa);
            txtQcustName = (TextView) itemViewa.findViewById(R.id.txtqname);
            txtQCustNum = (TextView) itemViewa.findViewById(R.id.txtqcustNumber);
            txtQorderTotal = (TextView) itemViewa.findViewById(R.id.txtqtotal);
            txtQOrderDate = (TextView) itemViewa.findViewById(R.id.txtqdate);
            txtQStatus = (TextView) itemViewa.findViewById(R.id.txtqstatus);
            txtQOrderNum = (TextView) itemViewa.findViewById(R.id.txtqid);
            txtQCustAddr = (TextView) itemViewa.findViewById(R.id.txtqcustAddr);
            itemViewa.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {

            String shop_number = "";
            String shop_name = txtQcustName.getText().toString();
            String shop_addr = txtQCustAddr.getText().toString();
            String uniqueID = txtQOrderNum.getText().toString();

            SharedPreferences shared = context.getSharedPreferences("Order", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString("order_id", uniqueID);
            editor.putString("cust_number", shop_number);
            editor.putString("cust_name", shop_name);
            editor.putString("cust_addr", shop_addr);
            editor.commit();


            Intent intent= new Intent(context, Order.class);
            context.startActivity(intent);


// go to the new intent with the shop name and adress, and send it to database to create new invoive
//            Intent intent=new Intent(context.getApplicationContext(), Order.class);
//            intent.putExtra("cust_number", shop_number);
//            context.getApplicationContext().startActivity(intent);


        }
    }


}
