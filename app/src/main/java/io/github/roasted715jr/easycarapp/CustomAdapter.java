package io.github.roasted715jr.easycarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final int resourceID;
    private final int parentID;

    public CustomAdapter(Context context, int resource, int parent, ArrayList<String> bah) {
        super(context, resource, bah);

        this.context = context;
        this.resourceID = resource;
        this.parentID = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resourceID, parent, false);

        return view;
    }
}
