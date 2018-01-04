package org.androidtown.randomsite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninInterface extends AppCompatActivity {
    Button button_start;
    EditText editText_mail;
    EditText editText_password;
    CheckBox checkbox_check;
    String usr_mail;
    String usr_password;
    Bundle extra;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_interface);

        mAuth = FirebaseAuth.getInstance();

        button_start = (Button) findViewById(R.id.button_start);
        editText_mail = (EditText) findViewById(R.id.editText_url);
        editText_password = (EditText) findViewById(R.id.editText_password);
        checkbox_check = (CheckBox) findViewById(R.id.checkBox_agree);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_check.isChecked() == true) {
                    usr_mail = editText_mail.getText().toString();
                    usr_password = editText_password.getText().toString();
                    if (usr_mail != null && usr_password != null) {
                        mAuth.createUserWithEmailAndPassword(usr_mail, usr_password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SigninInterface.this, "DONE!!", Toast.LENGTH_SHORT).show();
                                            extra = new Bundle();
                                            Intent intent = new Intent();
                                            extra.putInt("Sign", 1);
                                            intent.putExtras(extra);
                                            SigninInterface.this.setResult(1009, intent);
                                            SigninInterface.this.finish();
                                        } else {
                                            Toast.makeText(SigninInterface.this, "Sorry.. Something is failled!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(SigninInterface.this, "Please check the box!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
