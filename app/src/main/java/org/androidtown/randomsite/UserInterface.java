package org.androidtown.randomsite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.lang.reflect.Field;
import java.util.Random;

public class UserInterface extends AppCompatActivity {

    private InterstitialAd interstitialAd;
    TextView textView_notice; // notice
    TextView textView_target;
    TextView textView_address;
    TextView textView_UserReview;
    EditText editText_userReview;
    String strNotice = null;
    int num_Notice;
    int count;
    int usr_const;
    Button But_Target;
    Button But_Run;
    Button But_Go;
    Button button_send;
    FirebaseDatabase database;
    FirebaseUser currentUser;
    DatabaseReference noticeRef;
    DatabaseReference erRef;
    DatabaseReference nowRef;
    private FirebaseAuth mAuth;
    Random random = new Random();
    final CharSequence[] items = { "Shopping mall", "Home Page", "Multimedia", "Graphics", "IT"};
    String Chosen = null;

    // Title setting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        usr_const = 5;
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3302287697444700~1922687818");
        setFullAd(); // ad setting
        mAuth = FirebaseAuth.getInstance();
        count = 0;

        textView_notice = (TextView) findViewById(R.id.textview_notice);
        textView_target = (TextView) findViewById(R.id.textView_TARGET);
        textView_address = (TextView) findViewById(R.id.textView_address);
        textView_UserReview = (TextView) findViewById(R.id.textView_Review);

        textView_UserReview.setMovementMethod(new ScrollingMovementMethod());
        editText_userReview = (EditText) findViewById(R.id.editText_userReview);
        database = FirebaseDatabase.getInstance();
        noticeRef = database.getReference("Notice");
        erRef = database.getReference("WebSitesDB");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserInterface.this);
        But_Target = (Button) findViewById(R.id.button_TARGET_USERINTERFACE);
        But_Go = (Button) findViewById(R.id.button_GO_USERINTERFACE);
        But_Run = (Button) findViewById(R.id.button_RUN_USERINTERFACE);
        button_send = (Button) findViewById(R.id.button_send);

        //1 call back and go to delete
        noticeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num_Notice = dataSnapshot.child("NoticeNum").getValue(int.class);
                strNotice = "";
                for(int i=0; i<num_Notice; i++){
                    strNotice += dataSnapshot.child("Contents").child("Content" + String.valueOf(i + 1)).getValue(String.class);
                    strNotice += "  ";
                    //Content1, Content2 ...
                }

                Log.d("wonseok", "String = " + strNotice);
                if (strNotice != null) {
                    textView_notice.setText(strNotice);
                }
                if( strNotice == null){
                    textView_notice.setText("Sorry, no notice is found ㅠ^ㅠ");
                }
                textView_notice.setSelected(true);
                //setMarqueeSpeed(textView_notice, 2.3f, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        editText_userReview.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if( i == keyEvent.KEYCODE_ENTER ){
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText_userReview.getWindowToken(), 0);
                    editText_userReview.setText("");
                }
                return true;
            }
        });
        button_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                currentUser = mAuth.getCurrentUser();
                if(currentUser == null){
                    Toast.makeText(UserInterface.this, "Check if you login", Toast.LENGTH_LONG).show();
                }
                else{
                    final String check_address;
                    check_address = textView_address.getText().toString();
                    nowRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if( check_address.equals(dataSnapshot.child("Address").getValue().toString())){
                                String usr_review;
                                usr_review = editText_userReview.getText().toString();
                                if(usr_review.equals("")){
                                    Toast.makeText(UserInterface.this, "Write any messages please", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    int review_num;
                                    review_num = dataSnapshot.child("ReviewDB").child("ReviewNum").getValue(int.class);
                                    review_num++;
                                    nowRef.child("ReviewDB").child("Review" + review_num).child("Reviewer").setValue(mAuth.getCurrentUser().getEmail());
                                    nowRef.child("ReviewDB").child("Review" + review_num).child("content").setValue(usr_review);
                                    nowRef.child("ReviewDB").child("ReviewNum").setValue(review_num);
                                    textView_UserReview.append(mAuth.getCurrentUser().getEmail()+" : " +usr_review+"\n");

                                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(editText_userReview.getWindowToken(), 0);
                                    editText_userReview.setText("");

                                }
                            }
                            else{
                                Toast.makeText(UserInterface.this, "Please click the RUN", Toast.LENGTH_LONG).show();
                                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText_userReview.getWindowToken(), 0);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        But_Go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String sites;
                sites = textView_address.getText().toString();
                if(sites != null) {
                    Uri uri = Uri.parse(sites);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        But_Run.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                displayAD();
                if( Chosen == null){
                    Toast.makeText(getApplicationContext(), "Choose the target first please ><", Toast.LENGTH_LONG).show();
                }
                else{
                    erRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int total = dataSnapshot.child("SitesDB").child("TotalSitesNum").getValue(int.class);
                            int tot_sites = dataSnapshot.child("SitesDB").child("NumOfCategory").child(Chosen).getValue(int.class);
                            int check_tot = random.nextInt(tot_sites) + 1;
                            String tmp;

                            for(int i=1, check=1; check <= check_tot; i++) {
                                if( i > total ){
                                    Toast.makeText(getApplicationContext(), "Sorry, No Database Now.." ,Toast.LENGTH_LONG).show();
                                    textView_address.setText("");
                                    textView_UserReview.setText("");
                                    break;
                                }
                                Log.d("wonseok", "i = " + String.valueOf(i) + " check = " + String.valueOf(check) + " tot_sites = " + String.valueOf(tot_sites));
                                tmp = "Site" + String.valueOf(i);
                                String tmp_chosen = dataSnapshot.child("SitesDB").child(tmp).child("Classify").getValue(String.class);
                                Log.d("wonseok", "chosen : " + tmp_chosen + " RChosen : " + Chosen);
                                if( tmp_chosen.equals(Chosen) ){
                                    Log.d("wonseok", "chosen is correct with tmp_chosen");
                                    if(check == check_tot) {
                                        String link = dataSnapshot.child("SitesDB").child(tmp).child("Address").getValue(String.class);
                                        textView_address.setText(link);
                                        int RN = dataSnapshot.child("SitesDB").child(tmp).child("ReviewDB").child("ReviewNum").getValue(int.class);
                                        textView_UserReview.setText("");
                                        String tmp_review;
                                        String tmp_reviewContent;
                                        for(int j=1; j<=RN; j++){
                                            tmp_review = dataSnapshot.child("SitesDB").child(tmp).child("ReviewDB").child("Review" + String.valueOf(j)).child("Reviewer").getValue(String.class);
                                            tmp_reviewContent = dataSnapshot.child("SitesDB").child(tmp).child("ReviewDB").child("Review" + String.valueOf(j)).child("content").getValue(String.class);
                                            textView_UserReview.append(tmp_review + " : " + tmp_reviewContent + "\n");
                                        }
                                        nowRef = dataSnapshot.child("SitesDB").child(tmp).getRef();
                                        Log.d("wonseok", "nowRef = " + nowRef.toString());
                                        break;
                                    }
                                        check ++;
                                }
                                if( i>100) break;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        But_Target.setOnClickListener(new View.OnClickListener(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserInterface.this);
            @Override
            public void onClick(View view) {
                alertDialogBuilder.setTitle("Choose what you want to explore");
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Chosen = items[id].toString();
                                textView_target.setText(Chosen);
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }
        });
        //setMarqueeSpeed(textView_notice, 2.3f, false);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // to maintain screen (AUG 08, 20:10)
    }

    protected void setMarqueeSpeed(TextView tv, float speed, boolean speedlsMultiplier) {
        try {
            Field f = tv.getClass().getDeclaredField("mMarquee");
            f.setAccessible(true);
            Object marquee = f.get(tv);
            if (marquee != null) {
                Field mf = marquee.getClass().getDeclaredField("mScrollUnit");
                mf.setAccessible(true);
                float newSpeed = speed;
                if (speedlsMultiplier) {
                    newSpeed = mf.getFloat(marquee) * speed;
                }
                mf.setFloat(marquee, newSpeed);
                Log.d(this.getClass().getSimpleName(), String.format("%s marquee speed set to %f", tv, newSpeed));
            }
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), String.format("%s marquee speed set fail fail fail to %f", tv, 0));
        }
    }
    private void setFullAd(){
        interstitialAd = new InterstitialAd(this); //새 광고를 만듭니다.
        interstitialAd.setAdUnitId(getResources().getString(R.string.adID)); //이전에 String에 저장해 두었던 광고 ID를 전면 광고에 설정합니다.
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
        if(count > usr_const || count == 0) {
            if (interstitialAd.isLoaded()) { //광고가 로드 되었을 시
                interstitialAd.show(); //보여준다
                count = 1;
                usr_const += 3;
            }
        }
        count = count++;
    }
}
