package hr.fer.azzi.menze.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.azzi.menze.activitys.MainActivity;
import hr.fer.azzi.menze.adapters.MeniAdapter;
import hr.fer.azzi.menze.R;
import hr.fer.azzi.menze.classes.DateSaldo;


/**
 * Created by Azzaro on 23.12.2014..
 */
public class SaldoFragment extends Fragment {

    View view;
    TextView iconFa;
    Button prijavaButton;
    EditText email;
    EditText lozinka;
    EditText unosXice;
    Button provjeriSaldo;
    View headerView;

    ImageView slika;
    TextView korisnik;
    TextView trenutniSaldo;
    TextView danasnjiSaldo;
    ListView potrosnjaLV;
    Button odjaviMe;
    MeniAdapter arrayAdapter;

    String username;
    String password;


    private SharedPreferences settings;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    boolean nesto = false;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        settings = getActivity().getSharedPreferences("postavke",0);
        username = settings.getString("username", "");
        password = settings.getString("password", "");
        nesto = settings.getBoolean("logged", false);
        if(nesto){
            view = inflater.inflate(R.layout.fragment_xica_prikaz, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_xica_login, container, false);
        }

        setViews();
        setClickListeners(nesto);
        return view;
    }



    private void setClickListeners(boolean isLogged) {
        iconFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) SaldoFragment.this.getActivity()).showMenu();
            }
        });

        if(isLogged){

            new GetXica().execute();

            odjaviMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reCreate(false);
                }
            });
        } else{

            if(username != null && password != null){
                email.setText(username);
                lozinka.setText(password);
            }

            provjeriSaldo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String brojXString = unosXice.getText().toString();
                    if(brojXString.length() < 19){
                        Toast.makeText(getActivity(), "krivi broj Xice",Toast.LENGTH_LONG).show();
                        return;
                    }

                    new getSaldo().execute(brojXString);
                }
            });

            prijavaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(email.getText().toString().matches(EMAIL_PATTERN) && lozinka.getText().toString().length() > 3){
                        nesto = true;
                        username = email.getText().toString();
                        password = lozinka.getText().toString();
                        reCreate(true);
                    }
                }
            });
        }
    }

    private void reCreate(boolean logged) {
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putString("username", username);
        localEditor.putString("password", password);
        localEditor.putBoolean("logged", logged);
        localEditor.apply();

        ((MainActivity) this.getActivity()).mjestoPageAdapter.notifyDataSetChanged();

    }

    public void setViews() {
        headerView = getActivity().getLayoutInflater().inflate(R.layout.xica_header, null);
        prijavaButton = (Button) view.findViewById(R.id.prijavaButton);
        email = (EditText) view.findViewById(R.id.etUserName);
        lozinka = (EditText) view.findViewById(R.id.etPass);
        unosXice = (EditText) view.findViewById(R.id.unosXice);
        provjeriSaldo = (Button) view.findViewById(R.id.provjeriSaldoButton);
        iconFa = (TextView) view.findViewById(R.id.icon_fa);
        slika = (ImageView) headerView.findViewById(R.id.slika);
        korisnik = (TextView) headerView.findViewById(R.id.imePrezime);
        trenutniSaldo = (TextView) headerView.findViewById(R.id.trenutniSaldo);
        danasnjiSaldo = (TextView) headerView.findViewById(R.id.danasnjiSaldo);
        odjaviMe = (Button) view.findViewById(R.id.odjaviMeButton);
        potrosnjaLV = (ListView) view.findViewById(R.id.potrosnjaLV);
        if (potrosnjaLV != null) {
            potrosnjaLV.addHeaderView(headerView);
        }


        Typeface fontFamily = Typeface.createFromAsset(getActivity().getResources().getAssets(), "fontawesome.ttf");
        iconFa.setTypeface(fontFamily);
        iconFa.setText("\uf0c9 ");


    }

    private class getSaldo extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            try {
                Document doc = Jsoup.connect("http://www.cap.srce.hr/saldo.aspx?brk=" + strings[0] ).timeout(0).get();
                String docText = doc.text();
                String beg = "Preostali saldo:";

                return docText.substring(docText.indexOf(beg) + beg.length(),
                        docText.indexOf("Status kartice:")).trim().replace(',','.');
            } catch (IOException e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String res) {
            if(getActivity() != null){
                if (res == null){
                    Toast.makeText(getActivity(), "krivi broj Xice",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Trenutni saldo: " + res,Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    class GetXica extends AsyncTask<String, Void, String>
    {
        Bitmap bmImg;
        String imePrezime;
        String dSaldo;
        String mjSaldo;
        List<String> lista;
        @Override
        protected String doInBackground(String... paramVarArgs)
        {
            lista = new ArrayList<>();
            DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
            HttpPost localHttpPost = new HttpPost("http://sokac.net/xica/xica.php");
            try
            {
                ArrayList localArrayList = new ArrayList(2);
                localArrayList.add(new BasicNameValuePair("username", username));
                localArrayList.add(new BasicNameValuePair("password", password));
                localHttpPost.setEntity(new UrlEncodedFormEntity(localArrayList));
                String res = EntityUtils.toString(localDefaultHttpClient.execute(localHttpPost).getEntity());

                JSONTokener localJSONTokener = new JSONTokener(res);
                try {
                    JSONObject localJSONObject = (JSONObject)localJSONTokener.nextValue();
                    imePrezime = localJSONObject.getString("korisnik");
                    double d3 = localJSONObject.getDouble("stanje");
                    double d4 = localJSONObject.getDouble("ostalo");
                    dSaldo = String.valueOf(d4);
                    mjSaldo = String.valueOf(d3);

                    String slikaString =  "http://www.cap.srce.hr" + localJSONObject.getString("slika");
                    HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(slikaString).openConnection();
                    localHttpURLConnection.setDoInput(true);
                    localHttpURLConnection.connect();
                    InputStream localInputStream = localHttpURLConnection.getInputStream();
                    this.bmImg = BitmapFactory.decodeStream(localInputStream);
                    localInputStream.close();

                    JSONArray localJSONArray = localJSONObject.getJSONArray("racuni");

                    for (int i = localJSONArray.length(); i > 0; i--){
                        String imeRestorana = localJSONArray.getJSONObject(i - 1).getString("restoran");
                        String vrijeme = localJSONArray.getJSONObject(i - 1).getString("vrijeme");
                        String iznos = localJSONArray.getJSONObject(i - 1).getString("iznos") + "kn";
                        lista.add(imeRestorana + "\t" + vrijeme + "\t" + iznos);
                    }
                } catch (Exception e){
                }
            }
            catch (IOException localIOException)
            {
                localIOException.printStackTrace();
            }
            return "net";
        }


        @Override
        protected void onPostExecute(String res) {
            if(slika == null || korisnik == null || danasnjiSaldo == null || trenutniSaldo == null){
                return;
            }
            if (bmImg != null)
                slika.setImageBitmap(bmImg);

            if(imePrezime == null || danasnjiSaldo == null || trenutniSaldo == null){
                reCreate(false);
                return;
            }
            korisnik.setText(imePrezime);
            danasnjiSaldo.setText("Današnji saldo: " + dSaldo);
            trenutniSaldo.setText("Saldo: " + mjSaldo);

            if(potrosnjaLV != null && getActivity() != null){
                arrayAdapter = new MeniAdapter(getActivity(), R.layout.row_meni_layout, lista);
                potrosnjaLV.setAdapter(arrayAdapter);
            }


        }
    }

}
