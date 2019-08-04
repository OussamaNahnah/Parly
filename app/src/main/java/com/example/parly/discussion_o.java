package com.example.parly;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class discussion_o {
    String id;
      RecyclerView recyclerView;
      discusionAdaptor discusionAdapter;
 static int i=0;
    static ArrayList<User> users;

    public discussion_o(final Context context, View root, String mid) {
        users = new ArrayList<User>();
        recyclerView = (RecyclerView) root.findViewById(R.id.discussionlist);
        this.id = mid;
        FirebaseDatabase.getInstance().getReference("User").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    i++;
              //      FirebaseDatabase.getInstance().getReference().child("userd"+i).setValue(user.getUsername());
                    users.add(user);
                    discusionAdapter = new discusionAdaptor(context, users);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(discusionAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
