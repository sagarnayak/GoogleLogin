package com.csm.library.googlelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/*
author - sagar kumar nayak
date   - 6 aug 2016

this activity is an demo for how to use the Libclass for google login.

1. get a google client id and get the google services json from your account and set it in the app.
2. set the project level  dependency to classpath 'com.google.gms:google-services:3.0.0'
3. set the app level dependency to compile 'com.google.android.gms:play-services-auth:9.2.1' and include apply plugin: 'com.google.gms.google-services'
   at the bottom.
4. add the class Libclass to your package along side the activity.
5. set the layout of your activity to show the login button. add below layout to your activity as required.

    <com.csm.library.googlelogin.Libclass
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/libclass"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true" />

6. get a reference to this element in your java class.

    Libclass libclass;
    libclass = (Libclass) findViewById(R.id.libclass);

7. on your activity result write the below code -

      if (requestCode == Libclass.GOOGLE_SIGNIN_RESULT_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            libclass.setGoogleSignInResult(result);
       }

8. you are set. now you can use the reference of the element to get fifferent results.

      getResult();                   to get result we got from google after a success full login.
      logout();                      logout from the account.
      isGoogleSignInResultPresent(); to check if the result is empty of has any value.
 */
public class Main2Activity extends AppCompatActivity {

    Libclass libclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        libclass = (Libclass) findViewById(R.id.libclass);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("log", "at the activity result.");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Libclass.GOOGLE_SIGNIN_RESULT_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            libclass.setGoogleSignInResult(result);

            /*
            the thread is put here as a demo for automatic logout and getting the values from google. this is not necessary to use.
            you can use the logout and get values as you want.
             */
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (libclass.isGoogleSignInResultPresent()) {
                                    Log.i("log", "the google sign in results are :" + libclass.getresult().toString());
                                }
                                libclass.logout();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
}
