package com.parse.starter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
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
    TextView changeSignUpModeTextView;
    Boolean signUpModeActive = true;
    EditText passwordEditText;


    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(), UserListActivityMain.class);
            startActivity(intent);
    }


    public void signUp (View view){
        EditText usernameEditText = (EditText)findViewById(R.id.userName);
        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT ).show();
        }
        else {
            if(signUpModeActive){
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.i("Signup "," Sign up Successfull");
                            showUserList();
                        }
                        else
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user !=null){
                        Log.i("Signup","Login Successfull");

                        showUserList();
                    }
                    else{
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
        changeSignUpModeTextView = (TextView) findViewById(R.id.changeSignupModeTextView);

        changeSignUpModeTextView.setOnClickListener(this);
        RelativeLayout backgroundRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        ImageView logoImageView = (ImageView)findViewById(R.id.LogoImageView);

        backgroundRelativeLayout.setOnClickListener(this);
        logoImageView.setOnClickListener(this);

        passwordEditText = (EditText)findViewById(R.id.password);
        passwordEditText.setOnKeyListener(this);
        if(ParseUser.getCurrentUser() != null){
            showUserList();
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

    @Override
    public void onClick(View v) {
        if( v.getId()== R.id.changeSignupModeTextView){
            //Log.i("Appinfo","Change Signup Mode");
            Button signupButton = (Button)findViewById(R.id.signUpButton);
            if(signUpModeActive==true){
                signUpModeActive = false;
                signupButton.setText("Login");
                changeSignUpModeTextView.setText("or, SignUp");
            }
            else{
                signUpModeActive = true;
                signupButton.setText("SignUp");
                changeSignUpModeTextView.setText("or, LogIn");
            }


        }
        else if(v.getId() == R.id.relativeLayout || v.getId() == R.id.LogoImageView){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            signUp(v);
        }
        return false;
    }
}