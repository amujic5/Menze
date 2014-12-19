package hr.fer.azzi.menze;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hr.fer.azzi.menze.classes.Menza;


/**
 * Created by Azzaro on 27.10.2014..
 */
public class MenzeAdapter extends ArrayAdapter<Menza> {

    private Context context;
    private List<Menza> menze;
    private int resID;

    public MenzeAdapter(Context context, int resID, List<Menza> menze) {
        super(context, resID, menze);
        this.context = context;
        this.menze = menze;
        this.resID = resID;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resID, parent, false);

        Menza menza = menze.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.menza_image);
        if(menza.getIdSlike() != 0)
            imageView.setImageResource(menza.getIdSlike());

        TextView textView = (TextView) view.findViewById(R.id.nazivMenze);
        textView.setText(menza.getNaziv());

        return view;
    }
}
