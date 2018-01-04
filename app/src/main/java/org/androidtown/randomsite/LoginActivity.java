package org.androidtown.randomsite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText editText_mail;
    EditText editText_password;
    Button button_login;
    Button button_signin;
    String usr_mail;
    String usr_password;
    int REQUEST_SIGNININTERFACE = 2001;
    Bundle extra;

    private FirebaseAuth mAuth; //firebase user database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance(); // user priority setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button_login = (Button) findViewById(R.id.button_login);
        button_signin = (Button) findViewById(R.id.button_signin);
        editText_mail = (EditText) findViewById(R.id.editText_url);
        editText_password = (EditText) findViewById(R.id.editText_password);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "WELCOME NEW USER~", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SigninInterface.class);
                startActivityForResult(intent, REQUEST_SIGNININTERFACE);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr_mail = editText_mail.getText().toString();
                usr_password = editText_password.getText().toString();

                mAuth.signInWithEmailAndPassword(usr_mail, usr_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    extra = new Bundle();
                                    Intent intent = new Intent();
                                    extra.putInt("Sign", 1);
                                    intent.putExtras(extra);
                                    LoginActivity.this.setResult(1009, intent);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please check ID or PW", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    }
}
