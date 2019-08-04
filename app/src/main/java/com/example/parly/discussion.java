package com.example.parly;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class discussion extends AppCompatActivity {

    CircleImageView friend_icon;
    TextView friend_name, friend_state;
    RelativeLayout activity_message;

    ImageButton send, delete_msgs;
    EditText message;
    String friendID = "";
    User friend = null;
    message_Adapter message_adapter;
    List<Chats> mchat;
    RecyclerView recyclerView;
    Intent intent;
    FirebaseUser firebaseUser;
    DatabaseReference reference, reference_friend;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        delete_msgs = (ImageButton) findViewById(R.id.icon_delete_msgs);
        MainActivity.status("Online");
        initStatusBar();
        final ArrayList<message> messages = new ArrayList<message>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);


        activity_message = (RelativeLayout) findViewById(R.id.activity_message);

        friend_icon = (CircleImageView) findViewById(R.id.friend_icon);
        friend_name = (TextView) findViewById(R.id.friend_name);
        friend_state = (TextView) findViewById(R.id.friend_state);
        send = (ImageButton) findViewById(R.id.btn_send);
        message = (EditText) findViewById(R.id.message);

        recyclerView = (RecyclerView) findViewById(R.id.chats_);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        try {

            friendID = getIntent().getExtras().getString("friendID");
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        MainActivity.BACKGROUND(user.getBackground(), activity_message);
                    } else Toast.makeText(getApplication(), "mkach", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            reference_friend = FirebaseDatabase.getInstance().getReference("User").child(friendID);
            reference_friend.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        friend = user;
                        friend_name.setText(user.getUsername());
                        friend_state.setText(user.getStatus());
                        if (user.getImgUrl().equals("default")) {
                            friend_icon.setImageResource(R.drawable.profile_icon);
                        } else {
                            Glide.with(getApplication()).load(user.getImgUrl()).into(friend_icon);
                        }
                        if (!user.getCompte().equals("Actif")) {
                            message.setInputType(InputType.TYPE_NULL);
                            message.setText("This compte was deleted");
                            send.setEnabled(false);
                        }
                        readmessage(firebaseUser.getUid(), user.getId(), user.getImgUrl());
                    } else Toast.makeText(getApplication(), "mkach", Toast.LENGTH_SHORT).show();
                    friend_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getApplication(), friend_profile.class);
                            intent.putExtra("friend_p", friendID);
                            startActivity(intent);
                        }
                    });
                    FirebaseDatabase.getInstance().getReference("Chats").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String key = ds.getKey();
                                String sender = ds.child("sender").getValue(String.class);
                                String receiver = ds.child("receiver").getValue(String.class);
                                String seen = ds.child("seen").getValue(String.class);


                                if ((sender.equals(friendID) && receiver.equals(firebaseUser.getUid())) ||
                                        (sender.equals(firebaseUser.getUid()) && receiver.equals(friendID))
                                ) {
                                    FirebaseDatabase.getInstance().getReference("Chats").child(key).child("seen").setValue("true");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                  /*  reference = FirebaseDatabase.getInstance().getReference("Chats");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                messages.clear();
                                String key = ds.getKey();
                                String city = ds.child("message").getValue(String.class);
                                messages.add(new message(city));

                            }
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chats chat = snapshot.getValue(Chats.class);


                               // if (chat.getSender().equals(firebaseUser.getUid())&&chat.getSender().equals(friendID)) {
                                  messages.add(new message(chat.getMessage()));
                               // }else if (chat.getSender().equals(friendID)&&chat.getSender().equals(firebaseUser.getUid())) {
                                  //  messages.add(new message(chat.getMessage()));
                                //}
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = message.getText().toString();
                    if (!msg.equals("") && friendID != "") {
                        sendMessage(firebaseUser.getUid(), friendID, msg);
                        message.setText("");
                    }
                }
            });


            send.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                User user = ds.getValue(User.class);
                                if (!user.getId().equals(firebaseUser.getUid())) {
                                    //if ( user.getStatus().equals("Online"))
                                       //  {}
                                        String msg = message.getText().toString();
                                        if (!msg.equals(""))// && friendID != ""
                                        {
                                            sendMessage(firebaseUser.getUid(), user.getId(), msg);


                                    }


                                }
                            }     message.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    return false;
                }
            });


        } catch (Exception e) {
        }

        delete_msgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String lastmsg = "default";
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String key = ds.getKey();
                            String msg = ds.child("message").getValue(String.class);
                            String sender = ds.child("sender").getValue(String.class);
                            String receiver = ds.child("receiver").getValue(String.class);
                            String seen = ds.child("seen").getValue(String.class);
                            if (sender.equals(firebaseUser.getUid())&& receiver.equals(friendID)){
                                FirebaseDatabase.getInstance().getReference("Chats").child(key).
                                        child("message").setValue("This message was deleted");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });

    }

    public void sendMessage(String sender, String receiver, String message) {
        // Chats chat = new Chats(sender, receiver, message);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(System.currentTimeMillis() + "");
/*
        reference.child("sender").setValue(sender);
        reference.child("receiver").setValue(receiver);
        reference.child("message").setValue(message);
    */
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("seen", "false");
        reference.setValue(hashMap);


    }

    private void initStatusBar() {
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

    private void readmessage(final String myid, final String friendID, final String imageurl) {
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
               /* for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chats chat=snapshot.getValue(Chats.class);
                    if((chat.getreceiver().equals(myid)&&chat.getSender().equals(friendID))||(chat.getSender().equals(myid)&&chat.getreceiver().equals(friendID)))
                    {
                        mchat.add(chat);
                    }

                    message_adapter=new message_Adapter(mchat,imageurl);
                    recyclerView.setAdapter(message_adapter);
                }
           */
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //  mchat.clear();
                    String key = ds.getKey();
                    String msg = ds.child("message").getValue(String.class);
                    String sender = ds.child("sender").getValue(String.class);
                    String receiver = ds.child("receiver").getValue(String.class);
                    if ((receiver.equals(myid) && sender.equals(friendID)) || (sender.equals(myid) && receiver.equals(friendID))) {


                        mchat.add(new Chats(sender, receiver, msg));
                    }

                    message_adapter = new message_Adapter(mchat, imageurl);
                    recyclerView.setAdapter(message_adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.status("Offline");

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.status("Online");
    }
}
