package com.example.parly;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class backgroundAdaptor  extends ArrayAdapter<background> {

    private static final String LOG_TAG = discusionAdaptor.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *  @param context        The current context. Used to inflate the layout file.
     * @param backgrounds A List of AndroidFlavor objects to display in a list
     */

    public backgroundAdaptor(Activity context, ArrayList<background> backgrounds) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, backgrounds);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView; background background = getItem(position);
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_background, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list


        // Find the TextView in the list_item.xml layout with the ID version_name
        RelativeLayout background__ = (RelativeLayout) listItemView.findViewById(R.id.relative_background);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
       background__.setBackgroundResource(background.getPhoto());

        TextView namabackground = (TextView) listItemView.findViewById(R.id.background_name);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView
        namabackground.setText(background.getName());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

}