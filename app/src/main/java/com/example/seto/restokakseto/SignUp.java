package com.example.seto.restokakseto;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.example.seto.restokakseto.Model.User;

public class SignUp extends AppCompatActivity {
    MaterialEditText edtPhone,edtName,edtPassword;
    Button btnSignUp;
    String TAG = "SignUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        //INIT FIREBASE
        final FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
        //KARENA SIGN UP BERHUBUNGAN DENGAN TABEL USER
        final DatabaseReference table_user = database.getReference("User");

        //KETIKA TAP SIGN UP
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("MENGAMBIL DATA....");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //CEK KETIKA NOMOR TELEPON SUDAH TERDAFTAR...LIHAT SYNTAX EXISTS()
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this,"NOMOR TELEPON SUDAH TERDAFTAR SEBELUMNYA", Toast.LENGTH_SHORT).show();
                        } else {

                            //KETIKA DATANYA BARU, BACA DATA DARI FIELD
                            //MASUKKAN KE TABEL USER
                            mDialog.dismiss();
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this,"REGRISTASI SUKSES !", Toast.LENGTH_SHORT).show();
                            finish();

                            //KETIKA BERHASIL, METHOD FINISH MEMBUAT KEMBALI KE MAIN ACTIVITY ( HALAMAN UTAMA )
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDialog.dismiss();
                        Log.d(TAG,database.toString());
                    }
                });

            }
        });
    }
}
