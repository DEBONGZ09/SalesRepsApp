package com.example.debongz.nextldsaadmin;

/**
 * Created by DEBONGZ on 1/5/2018.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AdapterCustomers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> custid;
    ArrayList<String> custname;
    ArrayList<String> custaddr;

    private Context context;
    private LayoutInflater inflater;
    //List<DataCustomers> data= Collections.emptyList();
    //DataCustomers current;
    int currentPos=0;


    // create constructor to initialize context and data sent from MainActivity
    public AdapterCustomers(Context context,
                            ArrayList<String> id,
                            ArrayList<String> sub_name,
                            ArrayList<String> Sub_full){
        this.context=context;
        inflater= LayoutInflater.from(context);
        //this.data=data;
        this.custid = id;
        this.custname = sub_name;
        this.custaddr = Sub_full;
    }



    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_customers, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        //DataCustomers current=data.get(position);
        myHolder.textCustName.setText(custname.get(position));
        myHolder.textAddr.setText(custaddr.get(position));
        myHolder.shopId.setText(custid.get(position));

    }
    // return total item from List
    @Override
    public int getItemCount() {
        return custid.size();

    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textCustName;
        TextView textAddr;
        TextView shopId;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textCustName = (TextView) itemView.findViewById(R.id.cust_nametxt);
            textAddr = (TextView) itemView.findViewById(R.id.cust_addr_txt);
            shopId = (TextView) itemView.findViewById(R.id.shopid);
            itemView.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {
            String shop_number = shopId.getText().toString();
            String shop_name = textCustName.getText().toString();
            String shop_addr = textAddr.getText().toString();
            String uniqueID = UUID.randomUUID().toString();

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
