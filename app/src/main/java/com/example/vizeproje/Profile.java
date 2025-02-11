package com.example.vizeproje;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private TextView lblNameReplyProfile, lblEmailReplyProfile, lblPasswwordReplyProfile, countDownTimerTextProfile;
    private ImageButton profileImageButton;
    private Button btnUpdateProfile, btnDeleteProfile;
    private Vibrator vibrator;
    private CountDownTimer countDownTimer;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageButton = (ImageButton) findViewById(R.id.profileImageButton);
        lblNameReplyProfile = (TextView) findViewById(R.id.lblNameReplyProfile);
        lblEmailReplyProfile = (TextView) findViewById(R.id.lblEmailReplyProfile);
        lblPasswwordReplyProfile = (TextView) findViewById(R.id.lblPasswordReplyProfile);
        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        btnDeleteProfile = (Button) findViewById(R.id.btnDeleteProfile);
        countDownTimerTextProfile = (TextView) findViewById(R.id.countDownTimerTextProfile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        assert mUser != null; // mUser null değilse devam edecek nullsa bildirim verir.
        bringTheData(mUser.getUid());



        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator();
                Intent myI = new Intent(Profile.this, Update.class);
                startActivity(myI);
            }
        });


        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteUserAlertDialog(Profile.this, "Uyarı", "Hesabınızı silmek istediğinize emin misiniz?");
            }
        });
    }





    private void bringTheData(String uid) {
        mReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(uid);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("Kullanıcı adı").getValue(String.class);
                    String email = snapshot.child("Kullanıcı Email").getValue(String.class);
                    String password = snapshot.child("Kullanıcı Parola").getValue(String.class);

                    lblNameReplyProfile.setText(name);
                    lblEmailReplyProfile.setText(email);
                    lblPasswwordReplyProfile.setText(password);
                } else {
                     Toast.makeText(Profile.this, "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void vibrator() {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final int vibrationTime = 100;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrationTime, VibrationEffect.DEFAULT_AMPLITUDE));
        }else
            vibrator.vibrate(vibrationTime);
    }


    public void showDeleteUserAlertDialog (Context context,String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                removeUser();
            }
        }).show();
    }


    public void removeUser() {
        mReference = FirebaseDatabase.getInstance().getReference("Kullanıcılar").child(mUser.getUid());
        mReference.removeValue()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (mUser != null) {
                                mUser.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    logout();
                                                } else {
                                                    Toast.makeText(Profile.this, "Kullanıcı silinirken bir hata oluştu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(Profile.this, "Mevcut kullanıcı bulunamadı.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                            Toast.makeText(Profile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }


    public void logout() {
        Toast.makeText(Profile.this, "Kullanıcı başarıyla silindi.", Toast.LENGTH_SHORT).show();

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                String timeLeftFormatted = String.format("%02d:%02d", seconds / 60, seconds % 60);
                countDownTimerTextProfile.setText("Kalan Süre: " + timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

}
