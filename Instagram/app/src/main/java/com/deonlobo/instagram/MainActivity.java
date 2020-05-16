package com.deonlobo.instagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Button signupButton;
    TextView changeSignupModeTextView;
    EditText passwordEditText;

    Boolean signUpModeActive = true;

    public void showUserList(){

        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){

            signUp(v);

        }

        return false;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.changeSignupModeTextView){

            signupButton = (Button) findViewById(R.id.signupButton);

            if(signUpModeActive){

                signUpModeActive = false;
                signupButton.setText("Login");
                changeSignupModeTextView.setText("Or Signup");


            }else{

                signUpModeActive = true;
                signupButton.setText("Signup");
                changeSignupModeTextView.setText("Or Login");

            }

        }else if(view.getId() == R.id.backgroundRelativeLayout || view.getId() == R.id.logoImageView){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }


    }

    public void signUp(View view){

        EditText userNameEditText = (EditText) findViewById(R.id.userNameEditText);

        if(userNameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){

            Toast.makeText(this, "User Name and password are required", Toast.LENGTH_SHORT).show();

        }else{

            if(signUpModeActive) {

                ParseUser user = new ParseUser();
                user.setUsername(userNameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            System.out.println("signup Successful");
                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }else{

                ParseUser.logInInBackground(userNameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {

                        if(user != null){

                            Log.i("Login"," Success");
                            showUserList();

                        }else{

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        changeSignupModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);

        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        passwordEditText.setOnKeyListener(this);

        changeSignupModeTextView.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }


        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }



}
