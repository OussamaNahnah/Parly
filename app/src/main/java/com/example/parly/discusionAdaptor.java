package com.example.parly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class discusionAdaptor extends RecyclerView.Adapter<discusionAdaptor.ViewHolder> {
    private Context context;
    private List<User> list;
    FirebaseUser firebaseUser;
    String key;
    String msg;
    String sender;
    String receiver;
   // int position;
    // static  int i=0;


    public discusionAdaptor(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo_profile;
        TextView username, lastmessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo_profile = (ImageView) itemView.findViewById(R.id.icon_discussion_list);
            username = (TextView) itemView.findViewById(R.id.name_discussion_list);
            lastmessage = (TextView) itemView.findViewById(R.id.msg_discussion_list);
        }
    }

    @NonNull
    @Override
    public discusionAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_descussion, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final User user = list.get(i);
       // i++;
       // FirebaseDatabase.getInstance().getReference().child("useradapter"+i).setValue(user.getUsername());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, discussion.class);
                i.putExtra("friendID", user.getId());
                context.startActivity(i); }
        });
        //position = i;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
                    if ((sender.equals(user.getId()) && receiver.equals(firebaseUser.getUid())) ||
                            (sender.equals(firebaseUser.getUid()) && receiver.equals(user.getId()))
                    ){                        lastmsg = msg;if (seen.equals("false"))
                                                viewHolder.lastmessage.setTextColor(Color.RED);
                    }
                }
                if (!lastmsg.equals("default")) {} else { }
                    viewHolder.lastmessage.setText(lastmsg);
                    viewHolder.username.setText(user.getUsername());
                    if (user.getImgUrl().equals("default"))
                        viewHolder.photo_profile.setImageResource(R.drawable.profile_icon);
                    else
                        Picasso.get().load(user.getImgUrl()).into(viewHolder.photo_profile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}