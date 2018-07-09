package com.example.debongz.nextldsaadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by DEBONGZ on 1/6/2018.
 */

public class AdapterOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataOrder> data= Collections.emptyList();
    DataOrder current;
    int currentPos=0;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterOrder(Context context, List<DataOrder> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_order, parent,false);
        AdapterOrder.MyHolder holder=new AdapterOrder.MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        AdapterOrder.MyHolder myHolder= (AdapterOrder.MyHolder) holder;
        DataOrder current=data.get(position);
        myHolder.txtOname.setText(current.OderName);
        myHolder.txtOunit.setText(current.OrderUnit);
        myHolder.txtOqty.setText(current.OrderQty);
        myHolder.txtOtotal.setText(current.OrderTotal);
        myHolder.txtOnum.setText(current.OrderNum);

    }
    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtOname;
        TextView txtOunit;
        TextView txtOqty;
        TextView txtOtotal;
        TextView txtOnum;

        // create constructor to get widget reference
        public MyHolder(View itemViewa) {
            super(itemViewa);
            txtOname = (TextView) itemViewa.findViewById(R.id.txtoname);
            txtOunit = (TextView) itemViewa.findViewById(R.id.txtounit);
            txtOqty = (TextView) itemViewa.findViewById(R.id.txtoqty);
            txtOtotal = (TextView) itemViewa.findViewById(R.id.txtototal);
            txtOnum = (TextView) itemViewa.findViewById(R.id.txtonumber);
            itemViewa.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {



            String item_number = txtOnum.getText().toString();
            String item_name = txtOname.getText().toString();
            String item_unit = txtOunit.getText().toString();
            String item_quantity = txtOqty.getText().toString();

            Intent intent= new Intent(context, OrderItem.class);
            intent.putExtra("item_number", item_number);
            intent.putExtra("item_name", item_name);
            intent.putExtra("item_unit", item_unit);
            intent.putExtra("item_quantity", item_quantity);
            context.startActivity(intent);


// go to the new intent with the shop name and adress, and send it to database to create new invoive
//            Intent intent=new Intent(context.getApplicationContext(), Order.class);
//            intent.putExtra("cust_number", shop_number);
//            context.getApplicationContext().startActivity(intent);


        }
    }


}
