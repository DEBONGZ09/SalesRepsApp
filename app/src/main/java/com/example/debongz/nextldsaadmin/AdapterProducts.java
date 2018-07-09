package com.example.debongz.nextldsaadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DEBONGZ on 1/6/2018.
 */

public class AdapterProducts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    ArrayList<String> prid;
    ArrayList<String> prname;
    ArrayList<String> prunit;

    private Context context;
    private LayoutInflater inflater;
//    List<DataProducts> data= Collections.emptyList();
//    DataProducts current;
    int currentPos=0;



    public AdapterProducts(Context context,
                            ArrayList<String> id,
                            ArrayList<String> name,
                            ArrayList<String> unit){
        this.context=context;
        inflater= LayoutInflater.from(context);
        //this.data=data;
        this.prid = id;
        this.prname = name;
        this.prunit = unit;
    }



    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_products, parent,false);
        AdapterProducts.MyHolder holder=new AdapterProducts.MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        AdapterProducts.MyHolder myHolder= (AdapterProducts.MyHolder) holder;
        //DataProducts current=data.get(position);

        myHolder.textProName.setText(prname.get(position));
        myHolder.textProUnit.setText(prunit.get(position));
        myHolder.proId.setText(prid.get(position));



    }
    // return total item from List
    @Override
    public int getItemCount() {
        return prid.size();
    }



    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textProName;
        TextView textProUnit;
        TextView proId;

        // create constructor to get widget reference
        public MyHolder(View itemViewa) {
            super(itemViewa);
            textProName = (TextView) itemViewa.findViewById(R.id.product_nametxt);
            textProUnit = (TextView) itemViewa.findViewById(R.id.unit_txt);
            proId = (TextView) itemViewa.findViewById(R.id.product_numbertxt);
            itemViewa.setOnClickListener(this);
        }



        // Click event for all items
        @Override
        public void onClick(View v) {



            String product_number = proId.getText().toString();
           String product_name = textProName.getText().toString();
            String product_unit = textProUnit.getText().toString();

            Intent intent= new Intent(context, OrderItem.class);
            intent.putExtra("item_number", product_number);
            intent.putExtra("item_name", product_name);
            intent.putExtra("item_unit", product_unit);
            intent.putExtra("item_quantity", "1");
            context.startActivity(intent);


// go to the new intent with the shop name and adress, and send it to database to create new invoive
//            Intent intent=new Intent(context.getApplicationContext(), Order.class);
//            intent.putExtra("cust_number", shop_number);
//            context.getApplicationContext().startActivity(intent);


        }
    }



}
