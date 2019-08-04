package com.example.parly;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class message_Adapter extends RecyclerView.Adapter<message_Adapter.ViewHolder>  {

    public  static final int MSG_TYPE_LEFT=0;
    public  static final int MSG_TYPE_RIGHT=1;

    private List<Chats> mchat;
    private String imageurl;

    FirebaseUser fuser;

    public message_Adapter(List<Chats> mchat,String imageurl){

        this.mchat=mchat;
        this.imageurl=imageurl;
    }


    @NonNull
    @Override
    public   ViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i==MSG_TYPE_RIGHT)
        {
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_right,viewGroup,false);
            return new message_Adapter.ViewHolder(view);
        }else{
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_left,viewGroup,false);
            return new message_Adapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chats chat=mchat.get(i);

        viewHolder.show_message.setText(chat.getMessage());
        if(imageurl.equals("default"))
            viewHolder.profile_image.setImageResource(R.drawable.profile_icon);
        else
            Picasso.get().load(imageurl).into(viewHolder.profile_image);
           // Glide.with().load(imageurl).into(viewHolder.profile_image);
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(fuser.getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}
