package com.pankaj.firebase.storage.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pankaj.firebase.storage.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pankaj at com.pankaj.firebase.storage.main on 09/11/17.
 * splash
 */

public class Splash extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonDatabase)
    void performDatabaseAction() {
        intent = new Intent(Splash.this, Database.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonStorage)
    void performStorageAction() {
        intent = new Intent(Splash.this, StorageActivity.class);
        startActivity(intent);
    }
}
