package com.example.parly;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class requestAdaptor extends RecyclerView.Adapter<requestAdaptor.ViewHolder> {
    private List<request_obj> request_objs;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    User user;
//private static final String LOG_TAG = requestAdaptor.class.getSimpleName();


    public requestAdaptor(Activity context, List<request_obj> request_objs) {
        this.request_objs = request_objs;


    }

/*@Override
public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
        listItemView = LayoutInflater.from(getContext()).inflate(
        R.layout.item_request, parent, false);
        }

        request_obj request_obj = getItem(position);

        ImageView request_icon = (ImageView) listItemView.findViewById(R.id.request_icon);
    //    request_icon.setImageResource(request_obj.getIcongroup());

        TextView username = (TextView) listItemView.findViewById(R.id.request_username);
        username.setText(request_obj.getReceiver());


        TextView bio = (TextView) listItemView.findViewById(R.id.request_bio);
       bio.setText(request_obj.getSender());

        return listItemView;
        }*/

    @NonNull
    @Override
    public requestAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_request, viewGroup, false);
        return new requestAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final requestAdaptor.ViewHolder viewHolder, int i) {
        final request_obj requestObj = request_objs.get(i);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user = ds.getValue(User.class);
                    assert user != null;
                    if (user.getId().equals(requestObj.getSender())) {
                        viewHolder.username.setText(user.getUsername());
                        viewHolder.bio.setText(user.getBio());
                        if (user.getImgUrl().equals("default"))
                            viewHolder.profile_image.setImageResource(R.drawable.profile_icon);
                        else
                            Picasso.get().load(user.getImgUrl()).into(viewHolder.profile_image);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewHolder.accepte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Friends").child(System.currentTimeMillis() + "");
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user1", firebaseUser.getUid());
                hashMap.put("user2", requestObj.getSender());
                reference.setValue(hashMap);

                final DatabaseReference reference2;

                reference2 = FirebaseDatabase.getInstance().getReference("Request");
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //  mchat.clear();

                            String key = ds.getKey();
                            String sender = ds.child("sender").getValue(String.class);
                            String receiver = ds.child("receiver").getValue(String.class);
                            assert sender != null;
                            assert receiver != null;
                            if ((sender.equals(requestObj.getSender()) && receiver.equals(firebaseUser.getUid()))) {
                                reference2.child(key).removeValue();
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference2;
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                reference2 = FirebaseDatabase.getInstance().getReference("Request");
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //  mchat.clear();

                            String key = ds.getKey();
                            String sender = ds.child("sender").getValue(String.class);
                            String receiver = ds.child("receiver").getValue(String.class);
                            if ((sender.equals(requestObj.getSender()) && receiver.equals(firebaseUser.getUid()))) {

                                FirebaseDatabase.getInstance().getReference("Request").child(key).removeValue();
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
    public int getItemCount() {
        return request_objs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView bio;
        public ImageView profile_image;
        public ImageButton accepte, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.request_username);
            profile_image = itemView.findViewById(R.id.request_icon);
            bio = itemView.findViewById(R.id.request_bio);
            accepte = itemView.findViewById(R.id.request_accepte);
            delete = itemView.findViewById(R.id.request_delete);


        }
    }
}
