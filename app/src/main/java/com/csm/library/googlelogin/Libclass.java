package com.csm.library.googlelogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sagar on 8/8/2016.
 * google login library class.
 * you can use this class to get the google login result for a user by writing less codes then you had to write with the original google
 * api.
 * i am using the google api internally but the codes are made into a class for ease of use.
 */
public class Libclass extends RelativeLayout implements GoogleApiClient.OnConnectionFailedListener {

    Context context;                                                            //to get the activity context.
    public static final int GOOGLE_SIGNIN_RESULT_CODE = 100;                    //status code for google result
    public static final String GOOGLE_SIGNIN_RESULT_NAME = "name";              //constant for name in googlesignin result.
    public static final String GOOGLE_SIGNIN_RESULT_EMAIL = "email";            //constant for email in googlesignin result.
    public static final String GOOGLE_SIGNIN_RESULT_ID = "id";                  //constant for id in googlesignin result.
    public static final String GOOGLE_SIGNIN_RESULT_PICTURE = "picture";        //constant for picture in googlesignin result.

    GoogleApiClient mGoogleApiClient;                                           //object of google api client.
    SignInButton signInButton;                                                  //object of google sign in button.
    GoogleSignInResult googleSignInResult;                                      //object to save google sign in result.

    /*
    constructor of the class , as it is a relative layout. we have to define the default constructors.
     */
    public Libclass(Context context) {
        super(context);
        this.context = context;          //the context of the class is initialised with the passed context to the constructor.
        initView();                      //init view is called to inflate and initialise the required objects for the functionality.
    }

    /*
    the default constructors of the relative layout.
     */
    public Libclass(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;          //the context of the class is initialised with the passed context to the constructor.
        initView();                      //init view is called to inflate and initialise the required objects for the functionality.
    }

    /*
    this function is used to inflate the layout and set it in the activity.
    it initialises the google sign in options, google api client, and sign in button.
    also the sign in button is customised here.
     */
    public void initView() {
        //to do the work of initialising the views
        LayoutInflater.from(context).inflate(R.layout.googlelogin, this, true);

        //initialise the google sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        //initialise a google api client and add the sign in options to it.
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) ((Activity) context), Libclass.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        //reference and customise the sign in button
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(googleSignInOptions.getScopeArray());


        /*
        setting listener for google sign in button. it will fire the google sign in procedure and the login will start.
        the account options will be shown and user can choose or add new account.
        when ever any sign in is done the activity result in the activity will be fired. and result will be returned.
        we have to send the result to this class from the activity result of the activity.
         */
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("log", "login button is clicked. event : " + v.getId());
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                ((Activity) context).startActivityForResult(signInIntent, GOOGLE_SIGNIN_RESULT_CODE);
            }
        });
    }

    /*
    this function will be fired when ever there is any error in google api client.
    this is a override method and we need not to call it from any where.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*
    this function will be called when we want to send the result of sign in to this class from activity.
    this function will initialise the google sign in result it got from the activity.
     */
    public void setGoogleSignInResult(GoogleSignInResult googleSigninResult) {
        this.googleSignInResult = googleSigninResult;
    }

    /*
    we can call this function to check weather the google sign in result is initialised or it is empty.
    if the result is not yet passed to the class from the activity, then this will be empty.
    and if the result is initialised then this will return true.
     */
    public boolean isGoogleSignInResultPresent() {
        if (googleSignInResult == null) {
            return false;
        } else {
            return true;
        }
    }

    /*
    this function is called to get the result in an arraylist. from this function we will get the values we got got from google after the successful login.
    this function will return the values in key value pairs, inside an arraylist.
     */
    public ArrayList<HashMap<String, String>> getresult() {
        if (googleSignInResult != null) {
            if (googleSignInResult.isSuccess()) {

                ArrayList<HashMap<String, String>> results = new ArrayList<>();
                HashMap<String, String> res = new HashMap<>();
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                res.put(GOOGLE_SIGNIN_RESULT_NAME, googleSignInAccount.getDisplayName());           //setting the name.
                results.add(res);
                res = new HashMap<>();
                res.put(GOOGLE_SIGNIN_RESULT_EMAIL, googleSignInAccount.getEmail());                //setting the email.
                results.add(res);
                res = new HashMap<>();
                res.put(GOOGLE_SIGNIN_RESULT_ID, googleSignInAccount.getId());                      //setting the id.
                results.add(res);
                res = new HashMap<>();
                res.put(GOOGLE_SIGNIN_RESULT_PICTURE, "" + googleSignInAccount.getPhotoUrl());      //setting the photo url.
                results.add(res);
                return results;                                                                     //return the results to the calling activity.
            } else {
                Log.i("log", "sign in result is fail.");                                            //if sign in result is fail then this will return null.
                return null;
            }
        } else {
            Log.i("log", "the googlesignin result does not have any values.");                      //if sign in result does not contain any values then this will return null.
        }
        return null;
    }

    /*
    this function will logout from the logged in account.
    if we want to explicitly logout of the logged in google account, then we have to simply call this function.
    this will do the logout process.
     */
    public void logout() {

        googleSignInResult = null;                                                                  //to nullify the existing values in the result.

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.toString().contains("SUCCESS")) {
                    Log.i("log", "logout was successful, status : " + status);
                } else {
                    Log.i("log", "logout was unsuccessful, status : " + status);
                }
            }
        });
    }
}
