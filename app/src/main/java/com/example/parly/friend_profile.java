package com.example.parly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class friend_profile extends AppCompatActivity {
    String friend_p;
    boolean t;
    LinearLayout friend_profile;
    FirebaseUser firebaseUser;
    DatabaseReference reference, reference_friend;
    ImageView friend_cover;
    Button send_request,back,accept,delete;
    CircleImageView friend_icon;
    TextView friend_username,friend_bio;
    RelativeLayout layout_request;
    Button unfriend;
    boolean user_friend=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        friend_profile=(LinearLayout)findViewById(R.id.friend_profile);
        unfriend=(Button) findViewById(R.id.btn_unfriend);
        unfriend.setVisibility(View.GONE);
        friend_cover=(ImageView)findViewById(R.id.cover_friend);
        friend_icon=(CircleImageView)findViewById(R.id.img_friend);
        friend_username=(TextView) findViewById(R.id.username_friend);
        friend_bio=(TextView) findViewById(R.id.bio_friend);
        back=(Button) findViewById(R.id.back);
        accept=(Button) findViewById(R.id.btn_accept);
        delete=(Button) findViewById(R.id.btn_delete);
        layout_request=(RelativeLayout)findViewById(R.id.layout_request);
        send_request=(Button) findViewById(R.id.btn_send_request);

        layout_request.setVisibility(View.GONE);


        try {
            friend_p = getIntent().getExtras().getString("friend_p");

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



            reference = FirebaseDatabase.getInstance().getReference("User").child(friend_p);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot ds=dataSnapshot;

                        String compte = ds.child("compte").getValue(String.class);
                        if(compte.equals("deleted")){
                            send_request.setVisibility(View.GONE);
                            unfriend.setVisibility(View.GONE);
                            layout_request.setVisibility(View.GONE);
                            user_friend=false;}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });























            reference = FirebaseDatabase.getInstance().getReference("Request");
             reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //  mchat.clear();

                        String key = ds.getKey();
                        String sender = ds.child("sender").getValue(String.class);
                        String receiver = ds.child("receiver").getValue(String.class);

                            if ((sender.equals(firebaseUser.getUid()) && receiver.equals(friend_p))) {
                                send_request.setText("Requested");
                                send_request.setClickable(false);
                                send_request.setVisibility(View.VISIBLE);
                                break;
                            } else {
                                if (sender.equals(friend_p) && receiver.equals(firebaseUser.getUid())) {
                                    send_request.setVisibility(View.GONE);
                                    layout_request.setVisibility(View.VISIBLE);
                                    break;
                                }

                            }




                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });









            reference = FirebaseDatabase.getInstance().getReference("Friends");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //  mchat.clear();

                        String key = ds.getKey();
                        String user1 = ds.child("user1").getValue(String.class);
                        String user2 = ds.child("user2").getValue(String.class);

                        if(!user_friend)
                        {
                            send_request.setVisibility(View.GONE);
                            unfriend.setVisibility(View.GONE);
                            layout_request.setVisibility(View.GONE);
                        }
                        else
                        {
                            if ((user1.equals(firebaseUser.getUid()) && user2.equals(friend_p)) || (user2.equals(firebaseUser.getUid()) && user1.equals(friend_p)) ) {
                                send_request.setText("Friend");
                                send_request.setVisibility(View.VISIBLE);
                                send_request.setClickable(false);
                                layout_request.setVisibility(View.GONE);
                                unfriend.setVisibility(View.VISIBLE);
                                break;
                            }
                        }


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





            reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        MainActivity.BACKGROUND(user.getBackground(), friend_profile);
                    } else Toast.makeText(getApplication(), "mkach", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            reference_friend = FirebaseDatabase.getInstance().getReference("User").child(friend_p);
            reference_friend.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);

                        friend_username.setText(user.getUsername());
                        friend_bio.setText(user.getBio());
                        if (user.getImgUrl().equals("default")) {
                            friend_icon.setImageResource(R.drawable.profile_icon);
                        } else {
                            Glide.with(getApplication()).load(user.getImgUrl()).into(friend_icon);
                        }
                        if (user.getCoverUrl().equals("default")) {
                            friend_cover.setImageResource(R.drawable.cover_icon);
                        } else {
                            Glide.with(getApplication()).load(user.getCoverUrl()).into(friend_cover);
                        }
                    } else Toast.makeText(getApplication(), "mkach", Toast.LENGTH_SHORT).show();
                    friend_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }catch (Exception e){
            Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();
        }





        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Request").child(System.currentTimeMillis() + "");


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("sender", firebaseUser.getUid());
                hashMap.put("receiver", friend_p);
                reference.setValue(hashMap);
//                reference.child("sender").setValue(firebaseUser.getUid());
//                reference.child("receiver").setValue(friend_p);
                send_request.setText("Requested");
                send_request.setClickable(false);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(),MainActivity.class);
               // intent.putExtra("friendID",friend_p);
                startActivity(intent);
            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends").child(System.currentTimeMillis() + "");


                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user1", firebaseUser.getUid());
                hashMap.put("user2", friend_p);
                reference.setValue(hashMap);
                send_request.setVisibility(View.VISIBLE);
                send_request.setText("Friend");
                send_request.setClickable(false);
                layout_request.setVisibility(View.GONE);


                final DatabaseReference reference2;

                reference2 = FirebaseDatabase.getInstance().getReference("Request");
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //  mchat.clear();

                            String key = ds.getKey();
                            String sender = ds.child("sender").getValue(String.class);
                            String receiver = ds.child("receiver").getValue(String.class);
                            if ((sender.equals(friend_p) && receiver.equals(firebaseUser.getUid()))) {
                                reference2.child(key).removeValue();
                                break;
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        unfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                reference = FirebaseDatabase.getInstance().getReference("Friends");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //  mchat.clear();

                            String key = ds.getKey();
                            String user1 = ds.child("user1").getValue(String.class);
                            String user2 = ds.child("user2").getValue(String.class);
                            if ((user1.equals(friend_p) && user2.equals(firebaseUser.getUid())) || (user2.equals(friend_p) && user1.equals(firebaseUser.getUid())))
                            {
                                reference.child(key).removeValue();
                                send_request.setText("Send Request");
                                send_request.setVisibility(View.VISIBLE);
                                send_request.setClickable(true);
                                layout_request.setVisibility(View.GONE);
                                unfriend.setVisibility(View.GONE);
                                break;
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Request");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //  mchat.clear();

                            String key = ds.getKey();
                            String sender = ds.child("sender").getValue(String.class);
                            String receiver = ds.child("receiver").getValue(String.class);
                            if ((sender.equals(friend_p) && receiver.equals(firebaseUser.getUid())))
                            {
                                reference.child(key).removeValue();
                                send_request.setText("Send Request");
                                send_request.setVisibility(View.VISIBLE);
                                send_request.setClickable(true);
                                layout_request.setVisibility(View.GONE);
                                break;
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplication(),MainActivity.class));
    }
    @Override
    protected void onPause() {
        super.onPause();

        MainActivity.  status("Offline");

    }

    @Override
    protected void onResume() {
        super.onResume();

           MainActivity. status("Online");
    }

}
