package com.example.vizeproje;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private TextView lblDateResultRegister;
    private EditText txtNameRegister, txtEmailAdressRegister, txtPasswordRegister, txtPasswordAgainRegister;
    private Button btnRegister, maps;
    private ImageButton btnOpenCalendar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtNameRegister = (EditText) findViewById(R.id.txtNameRegister);
        txtEmailAdressRegister = (EditText) findViewById(R.id.txtEmailAdressRegister);
        txtPasswordRegister = (EditText) findViewById(R.id.txtPasswordRegister);
        txtPasswordAgainRegister = (EditText) findViewById(R.id.txtPasswordAgainRegister);
        btnOpenCalendar = (ImageButton) findViewById(R.id.btnOpenCalendar);
        lblDateResultRegister = (TextView) findViewById(R.id.lblDateResultRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        maps = (Button) findViewById(R.id.maps);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();


        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myI = new Intent(Register.this, MapsActivity.class);
                startActivity(myI);
            }
        });

        btnOpenCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarView calendarView = new CalendarView(Register.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setView(calendarView);
                builder.setPositiveButton("Tamam", null);
                AlertDialog dialog = builder.create();
                dialog.show();

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        lblDateResultRegister.setText(selectedDate);
                        dialog.dismiss();
                    }
                });
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtName = txtNameRegister.getText().toString();
                String txtEmail = txtEmailAdressRegister.getText().toString();
                String txtPassword = txtPasswordRegister.getText().toString();
                String txtPasswordAgain = txtPasswordAgainRegister.getText().toString();

                if(txtName.equals("") || txtEmail.equals("") || txtPassword.equals("") || txtPasswordAgain.equals("")) {
                    Toast.makeText(getApplicationContext(), "Boşluk bırakmayınız.",Toast.LENGTH_SHORT).show();
                } else if (!(txtPassword.equals(txtPasswordAgain))) {
                    Toast.makeText(getApplicationContext(), "Parolalar eşleşmiyor.",Toast.LENGTH_SHORT).show();
                }else {
                    register(txtName, txtEmail, txtPassword);
                }
            }
        });
    }


    public void register(String txtName, String txtEmail, String txtPassword) {
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = mAuth.getCurrentUser();

                            mData = new HashMap<>();
                            mData.put("Kullanıcı adı", txtName);
                            mData.put("Kullanıcı Email", txtEmail);
                            mData.put("Kullanıcı Parola", txtPassword);
                            mData.put("Kullanıcı ID", mUser.getUid());

                            mReference.child("Kullanıcılar").child(mUser.getUid()).
                                    setValue(mData).addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Toast.makeText(Register.this, "Kayıt işlemi başarılı", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }else
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}