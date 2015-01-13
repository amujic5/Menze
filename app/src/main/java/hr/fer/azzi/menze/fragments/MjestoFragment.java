package hr.fer.azzi.menze.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.fer.azzi.menze.activitys.MainActivity;
import hr.fer.azzi.menze.activitys.MenzaDisplay;
import hr.fer.azzi.menze.adapters.MenzeAdapter;
import hr.fer.azzi.menze.R;
import hr.fer.azzi.menze.classes.Menza;


/**
 * Created by Azzaro on 17.12.2014..
 */
public class MjestoFragment extends Fragment {

    public static final String ID_MJESTA = "id_mjesta";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView iconFa = (TextView) view.findViewById(R.id.icon_fa);
        Typeface fontFamily = Typeface.createFromAsset(container.getResources().getAssets(), "fontawesome.ttf");
        iconFa.setTypeface(fontFamily);
        iconFa.setText("\uf0c9 ");
        iconFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) MjestoFragment.this.getActivity()).showMenu();
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null){
            int idMjesta = bundle.getInt(ID_MJESTA);

            setValues(view, idMjesta);
        }
        return view;
    }

    private void setValues(View view, int  idMjesta) {
        TextView textView = (TextView) view.findViewById(R.id.naziv_grada);

        Resources resources = view.getContext().getResources();

        String nazivMjesta = resources.getStringArray(R.array.mjesta)[idMjesta];
        textView.setText(nazivMjesta);

        int id = resources.obtainTypedArray(R.array.menze).getResourceId(idMjesta, 0);
        String[] menze = resources.getStringArray(id);
        final List<Menza> menzeList = new ArrayList<Menza>();
        for(String nazivMenze : menze){
            String[] atributiMenze = nazivMenze.split("\t");
            Menza menza = new Menza();
            menza.setNaziv(atributiMenze[0]);
            String packageName = view.getContext().getPackageName();
            menza.setIdSlike(resources.getIdentifier(atributiMenze[1], "drawable", packageName));
            menza.setIdOpis(resources.getIdentifier(atributiMenze[2], "array", packageName));
            menza.setLink(atributiMenze[3]);
            menza.setUlica(atributiMenze[4]);

            menzeList.add(menza);
        }

        MenzeAdapter menzeAdapter = new MenzeAdapter(view.getContext(),R.layout.row_menza_layout, menzeList);
        ListView listView = (ListView) view.findViewById(R.id.lista_menza);
        listView.setAdapter(menzeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), MenzaDisplay.class);
                intent.putExtra("menza", menzeList.get(position));
                startActivity(intent);
            }
        });


    }
}
