package com.example.parly;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class actifsList extends Fragment {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ArrayList<User> actifs;
    EditText search;
    List<User> users;


    public actifsList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_actifslist, container, false);
        actifs = new ArrayList<User>();


        /*
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));
        actifs.add(new actif_obj(R.drawable.photo,"Oussamanh"));

        actifAdapter actifAdapter = new actifAdapter(getActivity(), actifs);

        // Get a reference to the ListView, and attach the adapter to the listView.
        GridView gridView = (GridView)root.findViewById(R.id.gridview);
        gridView.setAdapter(actifAdapter);

*/
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                actifs.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;

                    if (!user.getId().equals(firebaseUser.getUid())) {
                        actifs.add(user);
                    }
                }
                actifAdapter actifAdapter = new actifAdapter(getActivity(), actifs);

                // Get a reference to the ListView, and attach the adapter to the listView.
                final GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
                gridView.setAdapter(actifAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = (User) gridView.getItemAtPosition(position);


                        // String friendID = getArguments().getString("friendID");
//                        Intent i = new Intent(getActivity(), discussion.class);
//                        i.putExtra("friendID", user.getId());
//                        startActivity(i);
                        Intent intent = new Intent(getContext(), friend_profile.class);
                        intent.putExtra("friend_p", user.getId());
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        search = (EditText) root.findViewById(R.id.search_actif);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchusers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void searchusers(String s) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("User").orderByChild("username").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                actifs.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;


                    if (!user.getId().equals(firebaseUser.getUid())) {
                        actifs.add(user);
                    }
                }
                actifAdapter actifAdapter = new actifAdapter(getActivity(), actifs);

                // Get a reference to the ListView, and attach the adapter to the listView.
                final GridView gridView = (GridView) getActivity().findViewById(R.id.gridview);
                gridView.setAdapter(actifAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
