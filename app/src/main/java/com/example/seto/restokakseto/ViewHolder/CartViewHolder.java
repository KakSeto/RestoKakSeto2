package com.example.seto.restokakseto.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seto.restokakseto.Interface.ItemClickListener;
import com.example.seto.restokakseto.R;

/** ------------- ISTILAHNYA FORMAT LIST VIEW HOLDER PADA CART -------------**/


//IMPLEMENT UNTUK MENGIMPLEMENTASIKAN SUATU INTERFACE KE DALAM SUATU CLASS
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txt_cart_name,txt_cart_price;
    public ImageView img_cart_count;

    private ItemClickListener itemClickListener;


    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    //KONSTRUKTOR
    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_cart_price=(TextView)itemView.findViewById(R.id.cart_item_Price);
        img_cart_count=(ImageView)itemView.findViewById(R.id.cart_item_count);

    }

    @Override
    public void onClick(View view) {

    }
}
