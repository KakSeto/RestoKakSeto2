package com.example.seto.restokakseto;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.seto.restokakseto.Common.Common;
import com.example.seto.restokakseto.Database.Database;
import com.example.seto.restokakseto.Model.Order;
import com.example.seto.restokakseto.Model.Request;
import com.example.seto.restokakseto.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //FIREBASE
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //DEKLARASI
        recyclerView = (RecyclerView)findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace =(Button)findViewById(R.id.btnPlaceOrder);


        //KETIKA TAP PLACE ORDER, PANGGIL METHOD SHOWALERT
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogue();
            }
        });

        //PANGGIL METHOD LOADLISTFOOD
        loadListFood();
    }
    private void showAlertDialogue()
    {
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(Cart.this);
        alertDialogue.setTitle("One More Step!");
        alertDialogue.setMessage("Enter Your Address:");

        final EditText editAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editAddress.setLayoutParams(lp);
        alertDialogue.setView(editAddress);
        alertDialogue.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //MENAMPILKAN OPSI YES / NO

        //KETIKA YES
        alertDialogue.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Request request = new Request(
                        Common.currentUse.getPhone(),
                        Common.currentUse.getName(),
                        editAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thanl You,Oder Placed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //KETIKA NO
        alertDialogue.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialogue.show();
    }


    //LOAD LIST FOOD YANG TELAH DIPESAN SEBELUMNYA
    private void loadListFood() {

        //MENGAMBIL DATA CART YANG BERISI PESANAN2
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //SEBAGAI COUNTER
        int total=0;

        //LOOPING SEBANYAK JUMLAH CART
        for (Order order:cart)
        {
            //PARSEINT = KONVERSI STRING KE INT

            //AMBIL HARGA, AMBIL QUANTITAS KALIKAN, LALU JUMLAHKAN " += " KARNA INI LOOPING
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

            //INI UNTUK SET IDR
            Locale locale = new Locale("en","ID");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

            //SET TOTAL DARI HITUNGAN KE TXTTOTAL PRICE
            txtTotalPrice.setText(numberFormat.format(total));
        }
    }
}
