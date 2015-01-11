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
public class MeniAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> lista;
    private int resID;

    public MeniAdapter(Context context, int resID, List<String> lista) {
        super(context, resID, lista);
        this.context = context;
        this.lista = lista;
        this.resID = resID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resID, parent, false);

        String[] strings = lista.get(position).split("\t");

        TextView imeRestorana = (TextView) view.findViewById(R.id.restoran);
        TextView datum = (TextView) view.findViewById(R.id.datum);
        TextView cijena = (TextView) view.findViewById(R.id.cijena);

        imeRestorana.setText(strings[0]);
        datum.setText(strings[1]);
        cijena.setText(strings[2]);

        return view;
    }
}
