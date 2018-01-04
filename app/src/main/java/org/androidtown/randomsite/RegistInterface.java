package org.androidtown.randomsite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistInterface extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private InterstitialAd interstitialAd;
    CheckBox check1;
    CheckBox check2;
    CheckBox check3;
    FirebaseUser currentUser;
    Button button1;
    EditText editText_mail;
    EditText editText_url;
    String usr_mail, usr_url;
    int count;
    int usr_const;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_interface);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("WebSitesDB").child("Registration");
        currentUser = mAuth.getCurrentUser();
        check1 = (CheckBox) findViewById( R.id.checkBox);
        check2 = (CheckBox) findViewById( R.id.checkBox2);
        check3 = (CheckBox) findViewById( R.id.checkBox3);
        button1 = (Button) findViewById( R.id.button4);
        editText_mail = (EditText) findViewById(R.id.editText_mail);
        editText_url = (EditText) findViewById( R.id.editText_url);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3302287697444700~1922687818");
        setFullAd();

        button1.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(currentUser == null){
                    Toast.makeText(RegistInterface.this, "Sorry, You have to sign in this apk first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(check1.isChecked() && check2.isChecked() && check3.isChecked()){
                    usr_mail = editText_mail.getText().toString();
                    usr_url = editText_url.getText().toString();

                    if(usr_mail != null && usr_url != null){
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int num;
                                num = dataSnapshot.child("RegistrationNum").getValue(int.class);
                                num++;
                                myRef.child("Registrations").child("Regist"+num).child("RegisterAddress").setValue(usr_url);
                                myRef.child("Registrations").child("Regist"+num).child("RegisterEMAIL").setValue(usr_mail);
                                myRef.child("RegistrationNum").setValue(num);

                                Toast.makeText(RegistInterface.this, "Okay! Please wait 1~2 days!", Toast.LENGTH_SHORT).show();
                                displayAD();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else{
                        if(usr_mail == null){
                            Toast.makeText(RegistInterface.this,"Your mail is needed!", Toast.LENGTH_LONG).show();
                        }
                        if(usr_url == null){
                            Toast.makeText(RegistInterface.this, "Your url is needed!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    if(check1.isChecked() == false){
                        Toast.makeText(RegistInterface.this, "You have to send mail to developer!", Toast.LENGTH_LONG).show();
                    }
                    if(check2.isChecked() == false){
                        Toast.makeText(RegistInterface.this, "Please, protect our childeren!", Toast.LENGTH_LONG).show();

                    }
                    if(check3.isChecked() == false){
                        Toast.makeText(RegistInterface.this, "Please, protect our future!", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        database = FirebaseDatabase.getInstance();
    }
    private void setFullAd(){
        interstitialAd = new InterstitialAd(this); //새 광고를 만듭니다.
        interstitialAd.setAdUnitId("ca-app-pub-3302287697444700/4263578992"); //이전에 String에 저장해 두었던 광고 ID를 전면 광고에 설정합니다.
        //AdRequest adRequest1 = new AdRequest.Builder().build(); //새 광고요청
        AdRequest adRequest1 = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest1); //요청한 광고를 load 합니다.
        interstitialAd.setAdListener(new AdListener() { //전면 광고의 상태를 확인하는 리스너 등록

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                displayAD();
            }

            @Override
            public void onAdClosed() { //전면 광고가 열린 뒤에 닫혔을 때
                AdRequest adRequest1 = new AdRequest.Builder().build();  //새 광고요청
                interstitialAd.loadAd(adRequest1); //요청한 광고를 load 합니다.
            }
        });
    }
    public void displayAD(){
        Log.d("wonseok", "count : " + count);
            if (interstitialAd.isLoaded()) { //광고가 로드 되었을 시
                interstitialAd.show(); //보여준다
                count = 1;
                usr_const += 2;
            }
    }
}
