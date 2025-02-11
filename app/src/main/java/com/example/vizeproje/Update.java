package com.example.vizeproje;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Update extends AppCompatActivity {
    private ImageButton profileImageButton;
    private EditText txtNameUpdate, txtEmailUpdate, txtPasswordUpdate;
    private Button btnUpdateUpdate;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        profileImageButton = (ImageButton) findViewById(R.id.profileImageButton);
        txtNameUpdate = (EditText) findViewById(R.id.txtNameUpdate);
        txtEmailUpdate = (EditText) findViewById(R.id.txtEmailUpdate);
        txtPasswordUpdate = (EditText) findViewById(R.id.txtPasswordUpdate);
        btnUpdateUpdate = (Button) findViewById(R.id.btnUpdateUpdate);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        assert mUser != null;
        bringTheData(mUser.getUid());

        btnUpdateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtNameUpdate.getText().toString()) && !TextUtils.isEmpty(txtEmailUpdate.getText().toString()) && !TextUtils.isEmpty(txtPasswordUpdate.getText().toString())) {
                    mData = new HashMap<>();
                    mData.put("Kullanıcı adı", txtNameUpdate.getText().toString());
                    mData.put("Kullanıcı Email", txtEmailUpdate.getText().toString());
                    mData.put("Kullanıcı Parola", txtPasswordUpdate.getText().toString());
                    updateData(mData, mUser.getUid());
                } else {
                    Toast.makeText(Update.this, "Güncellenecek değer boş olamaz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateData(HashMap<String, Object> hashMap, String uid) {
            mReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(uid);
            mReference.updateChildren(hashMap)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Update.this, "Veri başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                                bringTheData(mUser.getUid());
                            }
                        }
                    });
    }

    private void bringTheData(String uid) {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(uid);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Kullanıcı adı").getValue(String.class);
                    String email = snapshot.child("Kullanıcı Email").getValue(String.class);
                    String password = snapshot.child("Kullanıcı Parola").getValue(String.class);

                    txtNameUpdate.setText(name);
                    txtEmailUpdate.setText(email);
                    txtPasswordUpdate.setText(password);
                } else {
                    Toast.makeText(Update.this, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Update.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}