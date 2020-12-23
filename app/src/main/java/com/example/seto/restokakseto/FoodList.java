package com.example.seto.restokakseto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import com.example.seto.restokakseto.Interface.ItemClickListener;
import com.example.seto.restokakseto.Model.Food;
import com.example.seto.restokakseto.ViewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity {

    /**--------- LIST MAKANAN SETELAH KATEGORI ------------**/
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    //CLASS DATABASE DIPANGGIL SEBAGAI "database"
    FirebaseDatabase database;

    //CLASS DATABASEREFERENCE DIPANGGIL SEBAGAI "foodlist"
    DatabaseReference foodList;


    //UNTUK MENYIMPAN CATEGORI ID (KEY ) AGAR KETIKA MENU CATEGORY DI TAP, DENGAN MEMBAWA ID BISA MENAMPILKAN ID YANG DIMINTA
    String categoryId="";


    //ADAPDER
    //RECYCLER LEBIH BAIK DARI LISTVIEW
    //MERUPAKAN JEMBATAN VIEW DAN DATA , JUGA MENGELOLA MODEL, MAKANYA ADA PARAM MODEL FOOD
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //AMBIL DATA FIREBASE
        //KARENA BERHUBUNGAN DENGAN TABEL FOODS
        database= FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get intent here

        //GETSTRINGEXTRA DIGUNAKAN UNTUK MEMBAWA EXTRA KOMPONEN PADA SAAT PINDAH ACTIVITY
        //PASANGANNYA PASTI PUTEXTRA
        //DISINI YANG DIBAWA CATEGORY ID

        //UNTUK MENGHINDARI NULL
        //KALO GA LOLOS INI PASTI FORCE CLOSED KARENA NULL
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId !=null){

            //KETIKA SUDAH TIDAK NULL LOAD METHOD UNTUK MENAMPILKAN LIST MAKANAN
            loadListFood(categoryId);
        }

    }

    private void loadListFood(String categoryId) {

        //ADAPTER YG DIATAS TADI DIISI
        //PARAMETERNYA CLASS MODEL FOOD DAN FOODVIEWHOLDER
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class
        ,R.layout.food_item,FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)) { // seperti halnya  : select * from foods where menuid =
            @Override
            //DENGAN POPULATE VIEW HOLDER, CLASS VIEWHOLDER BISA DIPOPULATE ( MEMANGGIL BERULANG )
            //PARAMETERNYA SEBGAI BERIKUT
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                //MENGANBIL DATA NAMA MAKANAN
                viewHolder.food_name.setText(model.getName());

                //MENGAMBIL DATA GAMBAR MAKANAN
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);


                final Food local = model;

                //KETIKA MENU MAKANAN DI TAP
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //MENAMPILKAN TOAST SESUAI NAMA MAKANAN
                        Toast.makeText(FoodList.this,local.getName(), Toast.LENGTH_SHORT).show();

                        //PINDAH ACTIVITY KE FOOD DETAIL
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);

                        //PUT EXTRA UNTUK MEMBAWA KOMPONEN FOOD ID DARI ITEM YANG DI KLIK
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });

            }
        };
        //MENULIS DI LOG CAT
        Log.d("TAG",""+adapter.getItemCount());
        //SET ADAPTER pada recycler view
        recyclerView.setAdapter(adapter);
    }
}
