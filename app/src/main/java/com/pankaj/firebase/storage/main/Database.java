package com.pankaj.firebase.storage.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pankaj.firebase.storage.R;
import com.pankaj.firebase.storage.utils.L;
import com.pankaj.firebase.storage.utils.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pankaj at com.pankaj.firebase.storage.main on 09/11/17.
 * database
 */

public class Database extends AppCompatActivity {

    private static final String TAG = "Database:-->";
    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }



    @OnClick(R.id.buttonSubmit)
    void performSubmitAction() {

        if (!TextUtils.isEmpty(edtUsername.getText().toString().trim()) && !TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
            readDatabase();
//            Long tsLong = System.currentTimeMillis() / 1000;
//            String userId = tsLong.toString();
//            writeNewUser(userId, edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());
        }
    }

    private void readDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postRef = database.getReference("login");
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user1 = data.getValue(User.class);
                    if (user1 != null) {
                        if (user1.email.equals(edtUsername.getText().toString().trim())) {
                            L.generalOkAlert(Database.this, "User already exist....");
                            return;
                        } else {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            String userId = tsLong.toString();
                            writeNewUser(userId, edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @OnClick(R.id.buttonReadData)
    void performReadDataAction() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postRef = database.getReference("login");
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user1 = data.getValue(User.class);
                    if (user1.email.equals(edtUsername.getText().toString().trim())) {
                        L.generalOkAlert(Database.this, "User already exist....");
                    } else {
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String userId = tsLong.toString();
                        writeNewUser(userId, edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void writeNewUser(String userId, String email, String password) {
        User user = new User(email, password);
        mDatabase.child("login").child(userId).setValue(user);
        edtUsername.setText("");
        edtPassword.setText("");

    }

    private void readUserData() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
