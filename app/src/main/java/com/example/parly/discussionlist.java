package com.example.parly;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class discussionlist extends Fragment {


    discusionAdaptor discusionAdapter;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String id;
    RecyclerView recyclerView;
    ArrayList<discussion_o> discussion_o;


    View root;
    public discussionlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       root = inflater.inflate(R.layout.fragment_discussion_list, container, false);


        recyclerView = (RecyclerView) root.findViewById(R.id.discussionlist);

      getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        discussion_o = new ArrayList<discussion_o>();
        try {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase.getInstance().getReference("Friends").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    discussion_o.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        id = null;
                        String user1 = ds.child("user1").getValue(String.class);
                        String user2 = ds.child("user2").getValue(String.class);
                        if (user1.equals(firebaseUser.getUid())) {


                            discussion_o.add(    new discussion_o(getActivity(),root,user2));
                        } else if (user2.equals(firebaseUser.getUid())) {


                            discussion_o.add( new discussion_o(getActivity(),root,user1));
                        }
                    }

//                    discusionAdapter = new discusionAdaptor(getActivity(), discussion_objs, id);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    recyclerView.setAdapter(discusionAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return root;
    }







    /*
    private void lastmessage(final String userID, final String photo_profile, final TextView last_msg)
    {
        theLastMessage="default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Chats chat=snapshot.getValue(Chats.class);
                    if((chat.getSender().equals(firebaseUser.getUid()) && chat.getreceiver().equals(userID))||(chat.getSender().equals(userID) && chat.getreceiver().equals(firebaseUser.getUid())))
                    {
                        theLastMessage=chat.getMessage();
                    }
                }
                switch (theLastMessage)
                {
                    case "default":{
                        last_msg.setText("No message");
                        break;
                    }
                    default:{
                        last_msg.setText(theLastMessage);
                        break;
                    }
                }
                if(photo_profile.equals("default"))
                theLastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    */



}
