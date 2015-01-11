package hr.fer.azzi.menze.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Azzaro on 27.10.2014..
 */
public class MenuAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> items;
    private int resID;

    public MenuAdapter(Context context, int resID, List<String> items) {
        super(context, resID, items);
        this.context = context;
        this.items = items;
        this.resID = resID;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resID, parent, false);

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(items.get(position));

        return view;
    }
}
