package ir.tahasystem.music.app.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.MainActivity;


public class CustomAdapterOneItem extends ArrayAdapter<String> {

    private final MainActivity context;
    private int hidingItemIndex;
    String[] objects;

    public CustomAdapterOneItem(MainActivity context, int textViewResourceId, String[] objects, int hidingItemIndex) {
        super(context, textViewResourceId, R.id.text1, objects);
        ;
        this.hidingItemIndex = hidingItemIndex;
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (position == 0) {
            LinearLayout tv = new LinearLayout(getContext());
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            v = super.getDropDownView(position, null, parent);
        }
        return v;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_item_login, parent,
                false);
        TextView txt = (TextView) row.findViewById(R.id.text1);
        txt.setText(objects[position]);


        return row;
    }


}