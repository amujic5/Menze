package hr.fer.azzi.menze;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.fer.azzi.menze.classes.Menza;

/**
 * Created by Azzaro on 17.12.2014..
 */
public class MjestoFragment extends Fragment {

    public static final String ID_MJESTA = "id_mjesta";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

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
            menza.setIdSlike(resources.getIdentifier(atributiMenze[1] , "drawable", view.getContext().getPackageName()));

            if(atributiMenze.length > 2){
                menza.setIdOpis(resources.getIdentifier(atributiMenze[2] , "array", view.getContext().getPackageName()));
            }
            if(atributiMenze.length == 4){
                menza.setLink(atributiMenze[3]);
            }

            menzeList.add(menza);
            Log.d("test", nazivMenze);
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
