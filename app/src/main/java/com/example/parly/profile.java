package com.example.parly;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class profile extends Fragment {
    ImageView cover_icon;
    CircleImageView img_profile;
    TextView username, bio;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Button background_btn;

    private Button logout, delete_account, settings, resetPassword;

    public profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        cover_icon = (ImageView) root.findViewById(R.id.cover_icon);
        img_profile = (CircleImageView) root.findViewById(R.id.img_profile);
        username = (TextView) root.findViewById(R.id.username_profile);
        bio = (TextView) root.findViewById(R.id.bio_profile);
        background_btn = (Button) root.findViewById(R.id.change_background);
        resetPassword = (Button) root.findViewById(R.id.change_password);
        delete_account = (Button) root.findViewById(R.id.delete_account);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    username.setText(user.getUsername());
                    bio.setText(user.getBio());
                    if (user.getImgUrl().equals("default")) {
                        img_profile.setImageResource(R.drawable.profile_icon);
                    } else {
                        Glide.with(getActivity()).load(user.getImgUrl()).into(img_profile);
                    }

                    if (user.getCoverUrl().equals("default")) {
                        cover_icon.setImageResource(R.drawable.cover_icon);
                    } else {
                        Glide.with(getActivity()).load(user.getCoverUrl()).into(cover_icon);
                    }

                } else Toast.makeText(getActivity(), "mkach", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout = (Button) root.findViewById(R.id.logOut);
        settings = (Button) root.findViewById(R.id.editprofile);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.delete_ac=true;
                FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("status").setValue("Offline");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), StartActivity.class));

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, new signup_setting());
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        background_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, new backgroundlist());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, new resetPassword());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.delete_ac=true;
                    FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("compte").setValue("deleted");
                    FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("bio").setValue("This compte was deleted");
                    FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("username").setValue("User");
                    FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("coverUrl").setValue("default");
                    FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("imgUrl").setValue("default");
                    FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid()).child("status").setValue("Offline");
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "User account deleted.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), StartActivity.class));
                                    }


                                }
                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().signOut();
                            Intent i=new Intent(getContext(),StartActivity.class);
                            startActivity(i);
                            //getActivity().finish();
                        }
                    });




                    reference = FirebaseDatabase.getInstance().getReference("Request");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds:dataSnapshot.getChildren()) {
                                String key=ds.getKey();
                                String sender=ds.child("sender").getValue(String.class);
                                String receiver=ds.child("receiver").getValue(String.class);
                                if (sender.equals(firebaseUser.getUid()) || receiver.equals(firebaseUser.getUid()))
                                {
                                    FirebaseDatabase.getInstance().getReference("Request").child(key).removeValue();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });













                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

}
