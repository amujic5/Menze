package hr.fer.azzi.menze.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hr.fer.azzi.menze.R;
import hr.fer.azzi.menze.classes.Menza;


/**
 * Created by Azzaro on 27.10.2014..
 */
public class MenzeAdapter extends ArrayAdapter<Menza> {

    private Context context;
    private int resID;

    private static class VH {
        ImageView image;
        TextView name;
        TextView street;
    }

    public MenzeAdapter(Context context, int resID, List<Menza> menze) {
        super(context, resID, menze);
        this.context = context;
        this.resID = resID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if(convertView != null) {
            view = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resID, parent, false);

            final VH viewHolder = new VH();
            viewHolder.image = (ImageView) view.findViewById(R.id.menza_image);
            viewHolder.name = (TextView) view.findViewById(R.id.nazivMenze);
            viewHolder.street = (TextView) view.findViewById(R.id.ulica);

            view.setTag(viewHolder);
        }

        final VH viewHolder = (VH) view.getTag();

        Menza menza = getItem(position);

        if(menza.getIdSlike() != 0)
            viewHolder.image.setImageResource(menza.getIdSlike());

        viewHolder.name.setText(menza.getNaziv());

        viewHolder.street.setText(menza.getUlica());

        return view;
    }
}
