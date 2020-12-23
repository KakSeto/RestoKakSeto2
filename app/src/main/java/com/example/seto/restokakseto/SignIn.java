package com.example.seto.restokakseto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.example.seto.restokakseto.Common.Common;
import com.example.seto.restokakseto.Model.User;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //INISIAL FIREBASE UNTUK MENCOCOKAN SIGN IN
        FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
        //karena sign in berhubungan dengan tabel user, maka get data dari user
        final DatabaseReference table_user = database.getReference("User");


        //KETIKA KLIK SIGN IN
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Mengambil Informasi....");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //APABILA USER TIDAK ADA DI TABEL
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()){
                            //get user information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if(user.getPassword().equals(edtPassword.getText().toString()))
                            {
                                Toast.makeText(SignIn.this,"SELAMAT DATANG",Toast.LENGTH_SHORT).show();
                                Intent homeIntent= new Intent(SignIn.this,Home.class);
                                Common.currentUse=user;
                                startActivity(homeIntent);
                                finish();

                            } else
                            {
                                Toast.makeText(SignIn.this,"PASSWORD ATAU NOMOR TELEPON SALAH", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this,"ANDA BELUM TERDAFTAR", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
