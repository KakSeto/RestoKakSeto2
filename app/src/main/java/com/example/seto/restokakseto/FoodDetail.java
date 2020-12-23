package com.example.seto.restokakseto;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.example.seto.restokakseto.Database.Database;
import com.example.seto.restokakseto.Model.Food;
import com.example.seto.restokakseto.Model.Order;

public class FoodDetail extends AppCompatActivity {

    /** --------- DETAIL MAKANAN YANG ADA TAMBAH DAN KURANG JUMLAH, HARGA, DESKRIPSI -----------**/

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageButton btnCart;
    ElegantNumberButton numberButton;

    //UNTUK MENAMPUNG FOOD ID YANG DIBAWA
    String foodId="";

    Food currentFood;
    FirebaseDatabase database;
    DatabaseReference foods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //SET VARIABEL YANG DIBUAT TADI

        //KARENA BERHUBUNGAN DENGAN TABEL FOODS MAKANYA PAKAI FOODS DIPARAMETER
        database = FirebaseDatabase.getInstance().getInstance();
        foods=database.getReference("Foods");

        //INIT VIRW
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (ImageButton) findViewById(R.id.btnCart);


        //KETIKA TEKAN BTNCART
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //DIBUAT PENAMPUNG UNTUK MENGIRIM DATA MAKANAN YANG DIPILIH ( BERDASAR FOOD ID )
                //DIINPUT KE CART ---> ORDER
                new Database(getBaseContext()).addToCart(new Order(foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()));

                Toast.makeText(FoodDetail.this,"Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        food_description = (TextView)findViewById(R.id.food_description);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //GET ID FOOD DARI INTENT
        //AGAR TIDAK NULL ISINYA
        if(getIntent()!=null){
            foodId = getIntent().getStringExtra("FoodId");
        }
        if(!foodId.isEmpty()){
            //KETIKA SUDAH TIDAK NULL
            getDetailFood(foodId);
        }


    }

    private void getDetailFood(String foodId) {

        //CHILD MERUPAKAN METHOD DARI DATABASEREFRENCE CLASS
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override

            //DATA SNAPSHOT MENGANDUNG DATA DARI FIREBASE, SETIAP KALI MENERIMA DATA DARI FIREBASE BENTUKNYA SNAPSHOT
            public void onDataChange(DataSnapshot dataSnapshot) {

                //UNTUK SET FOOD YANG DIPILIH
                currentFood = dataSnapshot.getValue(Food.class);

                //SET IMAGE
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(food_image);
                //SET NAMA DI COLLAPSING tOOLBAR
                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());

                food_description.setText(currentFood.getDescription());
            }

            //KETIKA ERROR DATABASE
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
