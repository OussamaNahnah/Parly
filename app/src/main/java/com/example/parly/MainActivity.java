package com.example.parly;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
   static DatabaseReference reference;
    RelativeLayout relativeLayout;
    boolean open_signup_setting = false;
  public static boolean delete_ac = false;

    private boolean doubleBackToExitPressedOnce = false;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.discussionlist:
                    fragment = new discussionlist();
                    loadFragment(fragment);
                    return true;
                case R.id.Users:
                    fragment = new actifsList();
                    loadFragment(fragment);
                    return true;
                case R.id.Request:
                    fragment = new request_list();
                    loadFragment(fragment);
                    return true;
                case R.id.profile:
                    fragment = new profile();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_main);
        try{




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        relativeLayout = (RelativeLayout) findViewById(R.id.container_);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);

                    // Toast.makeText(getActivity(), user.getBackground().toString(), Toast.LENGTH_SHORT).show();
                    BACKGROUND(user.getBackground(), relativeLayout);

                } else
                    Toast.makeText(getApplicationContext(), "errer 001", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {

            open_signup_setting = getIntent().getExtras().getBoolean("open_signup_setting");
            if (open_signup_setting) {
                loadFragment(new signup_setting());
            } else
                loadFragment(new discussionlist());
        } catch (Exception e) {
            loadFragment(new discussionlist());
        }



        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView mBottomNavigationView = findViewById(R.id.navigation);
        if (mBottomNavigationView.getSelectedItemId() == R.id.discussionlist) {
            if (doubleBackToExitPressedOnce) {

                super.onBackPressed();
                finish();
            } else
                Toast.makeText(this, "click double to exit !", Toast.LENGTH_SHORT).show();
            this.doubleBackToExitPressedOnce = true;
        } else {
            mBottomNavigationView.setSelectedItemId(R.id.discussionlist);
        }


    }

    /*
    private void initStatusBar() {
        Window window = this.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(ContextCompat.getColor(this, R.color.red));
    }*/
    private  void initStatusBar() {
        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setTintColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!delete_ac)
        status("Offline");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!delete_ac)
       status("Online");
    }


    public static void BACKGROUND(String background, View relativeLayout) {
        relativeLayout.setBackgroundResource(0);
        switch (background) {
            case "0":
                relativeLayout.setBackgroundResource(R.drawable.background2);
                break;
            case "1":
                relativeLayout.setBackgroundResource(R.drawable.background);
                break;
            case "2":
                relativeLayout.setBackgroundResource(R.drawable.background2);
                break;
            case "3":
                relativeLayout.setBackgroundResource(R.drawable.background3);
                break;
            case "4":
                relativeLayout.setBackgroundResource(R.drawable.background4);
                break;
            case "5":
                relativeLayout.setBackgroundResource(R.drawable.b1);
                break;
            case "6":
                relativeLayout.setBackgroundResource(R.drawable.b2);
                break;
            case "7":
                relativeLayout.setBackgroundResource(R.drawable.b3);
                break;
            case "8":
                relativeLayout.setBackgroundResource(R.drawable.b4);
                break;
            case "9":
                relativeLayout.setBackgroundResource(R.drawable.b5);
                break;
            case "10":
                relativeLayout.setBackgroundResource(R.drawable.b6);
                break;
            case "11":
                relativeLayout.setBackgroundResource(R.drawable.b7);
                break;
            case "12":
                relativeLayout.setBackgroundResource(R.drawable.b8);
                break;
            case "13":
                relativeLayout.setBackgroundResource(R.drawable.b9);
                break;
            case "14":
                relativeLayout.setBackgroundResource(R.drawable.b10);
                break;
            case "15":
                relativeLayout.setBackgroundResource(R.drawable.b11);
                break;
            case "16":
                relativeLayout.setBackgroundResource(R.drawable.b12);
                break;
            case "17":
                relativeLayout.setBackgroundResource(R.drawable.b13);
                break;
            case "18":
                relativeLayout.setBackgroundResource(R.drawable.b14);
                break;
            case "19":
                relativeLayout.setBackgroundResource(R.drawable.b15);
                break;
            case "20":
                relativeLayout.setBackgroundResource(R.drawable.b16);
                break;
            case "21":
                relativeLayout.setBackgroundResource(R.drawable.b17);
                break;
            case "22":
                relativeLayout.setBackgroundResource(R.drawable.b18);
                break;
            case "23":
                relativeLayout.setBackgroundResource(R.drawable.b19);
                break;

        }
    }
   static public void status(String status){
        reference.child("status").setValue(status);
    }
}