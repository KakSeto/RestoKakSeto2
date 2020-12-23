package com.example.seto.restokakseto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import com.example.seto.restokakseto.Common.Common;
import com.example.seto.restokakseto.Interface.ItemClickListener;
import com.example.seto.restokakseto.Model.Category;
import com.example.seto.restokakseto.ViewHolder.MenuViewHolder;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** ----- HOME DIAWALI DENGAN TAMPILAN KATEGORI MAKANAN -----**/


    //MEMANGGIL CLASS FIREBASE
    FirebaseDatabase database;
    DatabaseReference category;

    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    //MENAMPILKAN KATEGORI MAKANAN
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        setContentView(R.layout.activity_home);


        //DEKLARASI TOOLBAR DISAMPING
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //INIT FIREBASE
        //KARENA CLASS INI BERHUBUNGAN DENGAN CATEGORY MAKANAN, AMBIL DATA DARI TABEL CATEGORY
        database = FirebaseDatabase.getInstance();
        category=database.getReference("Category");

    //KALO DIPAKAI ERROR FORCE CLOSED FLOATING ACTION BUTTON
        // ASLINYA FLOATING ACTION DIGANTI BUTTOM AGAR BISA JALAN
     BottomNavigationView fab = (BottomNavigationView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
        @Override
          public void onClick(View view) {
             Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });


        // ----- BUKA TUTUP DRAWER DISAMPING ------------//
        //PANGGIL CLASS DRAWER
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // BELUM BISA TAMPIL
        // MENAMPILKAN NAMA USER DI DRAWER SAMPING

        View headerView = navigationView.getHeaderView(0);
        txtFullName=(TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUse.getName());

        //LOAD MENU
        recycler_menu =(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

    }

    private void loadMenu() {

        //MENAMPILKAN MENU KATEGOTI
        //AMBIL DATA DARI FIREBASE

        //PARAMETER
        //DIPANGGIL KE LAYOUT MENU_ITEM , CLASS MENUVIEWHOLDER, VARIABEL CATEGORY UNTUK SAMBUNG FIREBASE
        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
            viewHolder.txtMenuName.setText(model.getName());

                //MENAMPILKAN DATA GAMBAR DARI FIREBASE KE APLIKASI
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);

                //AGAR PER ITEM BISA DI TAP TANPA DIBERI ACTION SATU SATU
              final  Category clickItem =model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get CategoryId and send to new Activity
                        Intent foodList = new Intent(Home.this,FoodList.class);
                        //KARENA CategoryId ADALAH KEY, JADI AMBIL KEY DARI ITEM INI
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
            Intent menu_intent = new Intent(Home.this,Home.class);
            startActivity(menu_intent);
        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(Home.this,Cart.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_order) {
           // Intent orderIntent = new Intent(Home.this,OrderStatus.class);
            //startActivity(orderIntent);


        } else if (id == R.id.nav_log_out) {
            Intent SignIn= new Intent(Home.this,MainActivity.class);
            SignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(SignIn);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
