package org.androidtown.randomsite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
//  Coded by Wonseok
//  Started 2017.08.08 03:14 ~
//  Modeled at AUG. 06 / TEXT MESSAGE / RANDOM SITE ACCESSOR
*/
public class MainActivity extends AppCompatActivity {

    int REQUEST_REGISTINTERFACE = 1001;
    int REQUEST_USERINTERFACE = 1002;
    int REQUEST_LOGINACTIVITY = 1003;

    TextView textView_welcome;
    Button button_explore;
    Button button_come;
    Button button_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser;
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            textView_welcome.setText("Current user : " + "[not logined]");
        }
        else{
            textView_welcome.setText("Current user : " + mAuth.getCurrentUser().getEmail());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        button_explore = (Button) findViewById(R.id.button_explore);
        button_come = (Button) findViewById(R.id.button_come);
        button_login = (Button) findViewById(R.id.button_login);

        textView_welcome = (TextView) findViewById(R.id.textView_welcome);

        FirebaseUser currentUser;
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            textView_welcome.setText("Current user : " + currentUser.getEmail());
        }

        //user mode
        button_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserInterface.class);
                startActivityForResult(intent, REQUEST_USERINTERFACE);
            }
        });

        //registration mode
        button_come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistInterface.class);
                startActivityForResult(intent, REQUEST_REGISTINTERFACE);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGINACTIVITY);
            }
        });
    }
}
