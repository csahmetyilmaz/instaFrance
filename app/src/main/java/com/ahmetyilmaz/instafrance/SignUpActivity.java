package com.ahmetyilmaz.instafrance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText userNameText,passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userNameText=findViewById(R.id.signUpActivityUserText);
        passwordText=findViewById(R.id.signUpActivityPassword);

        //if user already logged in before -> directly go to next activity
        ParseUser parseUser=ParseUser.getCurrentUser();
        if (parseUser!=null){
            //intent -> if user sign in where to go
            Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
            startActivity(intent);
        }

    }
    public void signIn(View view){


        ParseUser.logInInBackground(userNameText.getText().toString(), passwordText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e !=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else{//if password is ok
                    Toast.makeText(getApplicationContext(),"Welcome"+user.getUsername(),Toast.LENGTH_LONG).show();
                    //intent -> if user sign in where to go
                    Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                    startActivity(intent);

                }
            }
        });

    }

    public void signUp(View view){

        ParseUser user=new ParseUser();
        user.setUsername(userNameText.getText().toString());
        user.setPassword(passwordText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){//if there is any error show it
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else{//if signup is OK say welcome
                    Toast.makeText(getApplicationContext(),"User Created",Toast.LENGTH_LONG).show();

                    //intent -> if user sign up where to go -> Send user to another activity with intent
                    Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
                    startActivity(intent);

                }
            }
        });
    }

}
