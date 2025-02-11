package com.example.vizeproje;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);


        EditText txtEmailAdressLogin, txtPasswordLogin;
        Button btnLogin, btnRegisterLogin;

        txtEmailAdressLogin = (EditText) findViewById(R.id.txtEmailAdressLogin);
        txtPasswordLogin = (EditText) findViewById(R.id.txtPasswordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegisterLogin = (Button) findViewById(R.id.btnRegisterLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = txtEmailAdressLogin.getText().toString();
                String txtPassword = txtPasswordLogin.getText().toString();

                if(txtEmail.equals("") || txtPassword.equals(""))
                    Toast.makeText(getApplicationContext(), "Boşluk bırakmayınız",Toast.LENGTH_SHORT).show();

                else
                {
                    mAuth.signInWithEmailAndPassword(txtEmail, txtPassword)
                            .addOnSuccessListener(Login.this, new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent myI = new Intent(Login.this, Anasayfa.class);
                                    myI.putExtra("emailAdress",txtEmailAdressLogin.getText().toString());
                                    startActivity(myI);

                                   /*
                                    System.out.println(mUser.getDisplayName());
                                    System.out.println(mUser.getEmail());
                                    System.out.println(mUser.getUid()); */
                                }


                            }).addOnFailureListener(Login.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myI = new Intent(Login.this, Register.class);
                startActivity(myI);
            }
        });
    }
}