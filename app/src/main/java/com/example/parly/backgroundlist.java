package com.example.parly;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class backgroundlist extends Fragment {
    RelativeLayout relativeLayout;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Drawable id_background = null;
    Boolean active=false;

    public backgroundlist() {
        // Required empty public constructor
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (id_background != null && (active==false)) {
            int sdk = android.os.Build.VERSION.SDK_INT;

            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                relativeLayout.setBackgroundDrawable(id_background);
            } else {
                relativeLayout.setBackground(id_background);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_backgroundlist, container, false);
        // reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.container_);
        id_background = relativeLayout.getBackground();
        final ArrayList<background> backgrounds = new ArrayList<background>();
        backgrounds.add(new background("Background 1", R.drawable.background, 1));
        backgrounds.add(new background("Background 2", R.drawable.background2, 2));
        backgrounds.add(new background("Background 3", R.drawable.background3, 3));
        backgrounds.add(new background("Background 4", R.drawable.background4, 4));
        backgrounds.add(new background("Photo 1", R.drawable.b1, 5));
        backgrounds.add(new background("Photo 2", R.drawable.b2, 6));
        backgrounds.add(new background("Photo 3", R.drawable.b3, 7));
        backgrounds.add(new background("Photo 4", R.drawable.b4, 8));
        backgrounds.add(new background("Photo 5", R.drawable.b5, 9));
        backgrounds.add(new background("Photo 6", R.drawable.b6, 10));
        backgrounds.add(new background("Photo 7", R.drawable.b7, 11));
        backgrounds.add(new background("Photo 8", R.drawable.b8, 12));
        backgrounds.add(new background("Photo 9", R.drawable.b9, 13));
        backgrounds.add(new background("Photo 10", R.drawable.b10, 14));
        backgrounds.add(new background("Photo 11", R.drawable.b11, 15));
        backgrounds.add(new background("Photo 12", R.drawable.b12, 16));
        backgrounds.add(new background("Photo 13", R.drawable.b13, 17));
        backgrounds.add(new background("Photo 14", R.drawable.b14, 18));
        backgrounds.add(new background("Photo 15", R.drawable.b15, 19));
        backgrounds.add(new background("Photo 16", R.drawable.b16, 20));
        backgrounds.add(new background("Photo 17", R.drawable.b17, 21));
        backgrounds.add(new background("Photo 18", R.drawable.b18, 22));
        backgrounds.add(new background("Photo 19", R.drawable.b19, 23));
        backgroundAdaptor backgroundAdaptor = new backgroundAdaptor(getActivity(), backgrounds);

        // Get a reference to the ListView, and attach the adapter to the listView.
        final ListView listView = (ListView) root.findViewById(R.id.background_);
        listView.setAdapter(backgroundAdaptor);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                active=true;
                background background = (background) listView.getItemAtPosition(position);
                relativeLayout.setBackgroundResource(background.getPhoto());
                Bundle arg = new Bundle();
                //arg.putString("back_ground", "" + background.getId());
                // String background_chosed = getArguments().getString("back_ground");
                reference.child("background").setValue("" + background.getId());
                Fragment fragment = new profile();
                fragment.setArguments(arg);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, fragment);

                transaction.addToBackStack(null);
                transaction.commit();

                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {active=false;
                background background = (background) listView.getItemAtPosition(position);
                relativeLayout.setBackgroundResource(background.getPhoto());

            }
        });


        return root;
    }

}
