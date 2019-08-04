package com.example.parly;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class request_list extends Fragment {
    private RecyclerView recyclerView;
List<request_obj> request_objs;
    FirebaseUser firebaseUser;
    TextView nbre_of_Inv;
    DatabaseReference reference;int NBRE_OF_INV=0;
    public request_list() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root=inflater.inflate(R.layout.fragment_requestlist, container, false);
        recyclerView=(RecyclerView)root.findViewById(R.id.requestlist);
            nbre_of_Inv=(TextView) root.findViewById(R.id.nbre_of_Inv);
        recyclerView.setHasFixedSize(true);
        request_objs = new ArrayList<>();



        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Request");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                request_objs.clear();
                NBRE_OF_INV=0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String receiver= ds.child("receiver").getValue(String.class);
                    String sender = ds.child("sender").getValue(String.class);

                    if(receiver.equals(firebaseUser.getUid())) {
                        NBRE_OF_INV++;
                        nbre_of_Inv.setText("You have "+NBRE_OF_INV+" invitation");
                        request_objs.add(new request_obj(sender, receiver));
                        requestAdaptor r = new requestAdaptor(getActivity(), request_objs);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(r);
                    }


            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return root;
    }

}
