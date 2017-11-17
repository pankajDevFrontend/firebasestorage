package com.pankaj.firebase.storage.main;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by pankaj at com.pankaj.firebase.storage.main on 09/11/17.
 * this is and application class
 */

public class CustomApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
