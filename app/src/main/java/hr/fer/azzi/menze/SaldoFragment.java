package hr.fer.azzi.menze;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import hr.fer.azzi.menze.classes.Korisnik;
import hr.fer.azzi.menze.classes.dao.KorisnikDao;

/**
 * Created by Azzaro on 23.12.2014..
 */
public class SaldoFragment extends Fragment {

    View view;
    Button provjeriButton;
    EditText upis;
    TextView prikazIznosa;
    TextView brojKartice;
    CheckBox zapamtiMe;
    Button zaboraviMe;
    KorisnikDao korisnikDao;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_provjera_salda_main, container, false);
        setViews();
        setVisibility(false);
        upis.setText("601983");


        korisnikDao = new KorisnikDao(container.getContext());
        final Korisnik korisnk = korisnikDao.get(1l);

        if(korisnk != null && korisnk.getId_x() != 0){
            setVisibility(true);
            prikazIznosa.setText(String.valueOf(korisnk.getSaldo()));
        }


        provjeriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaldoCheck().execute(upis.getText().toString());
            }
        });

        zaboraviMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                korisnikDao.delete(1L);
                setVisibility(false);
            }
        });
        return view;
    }

    public void setViews() {
        provjeriButton = (Button) view.findViewById(R.id.provjeriButton);
        upis = (EditText) view.findViewById(R.id.editText);
        prikazIznosa = (TextView) view.findViewById(R.id.prikazIznosa);
        brojKartice = (TextView) view.findViewById(R.id.broj_kartice_tv);
        zapamtiMe = (CheckBox) view.findViewById(R.id.zapamtime_cb);
        zaboraviMe = (Button) view.findViewById(R.id.zaboraviMe);
    }

    private void  setVisibility(boolean loggedIn){
        if(loggedIn){
            provjeriButton.setVisibility(View.GONE);
            upis.setVisibility(View.GONE);
            brojKartice.setVisibility(View.GONE);
            zapamtiMe.setVisibility(View.GONE);
            prikazIznosa.setVisibility(View.VISIBLE);
            zaboraviMe.setVisibility(View.VISIBLE);
        }else{
            brojKartice.setVisibility(View.VISIBLE);
            zapamtiMe.setVisibility(View.VISIBLE);
            provjeriButton.setVisibility(View.VISIBLE);
            upis.setVisibility(View.VISIBLE);
            prikazIznosa.setVisibility(View.INVISIBLE);
            zaboraviMe.setVisibility(View.INVISIBLE);
        }

   }

    private class SaldoCheck extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.cap.srce.hr/saldo.aspx?brk=" + strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String docText = doc.text();
            String beg = "Preostali saldo:";
            String res = docText.substring(docText.indexOf(beg) + beg.length(),
                    docText.indexOf("Status kartice:")).trim().replace(',','.');


            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            if(res.isEmpty()){
                prikazIznosa.setText("Krivi broj kartice");
                prikazIznosa.setVisibility(View.VISIBLE);
                return;
            }
            if(zapamtiMe.isChecked()){
                Korisnik korisnik = new Korisnik();
                korisnik.setId(1l);
                korisnik.setId_x(Long.parseLong(upis.getText().toString()));
                korisnik.setSaldo(Double.parseDouble(res));
                korisnikDao.insert(korisnik);
            }

            prikazIznosa.setText(res);
            setVisibility(true);
        }
    }
}
