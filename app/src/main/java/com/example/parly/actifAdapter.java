package com.example.parly;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class actifAdapter extends ArrayAdapter<User> {

    private static final String LOG_TAG = actifAdapter.class.getSimpleName();

    public actifAdapter(Activity context, ArrayList<User> users) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_actif, parent, false);
        }


        User user = getItem(position);
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.actif_membres);
      if (user.getImgUrl().equals("default")){
            iconView.setImageResource(R.drawable.profile_icon);
        }else
            Glide.with(getContext()).load(user.getImgUrl()).into(iconView);



        TextView nameTextView = (TextView) listItemView.findViewById(R.id.actif_username);
        nameTextView.setText(user.getUsername());
        TextView status = (TextView) listItemView.findViewById(R.id.status);
        status.setText(user.getStatus());
        return listItemView;
    }

}