package hr.fer.azzi.menze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import hr.fer.azzi.menze.classes.DateSaldo;
import hr.fer.azzi.menze.classes.Korisnik;
import hr.fer.azzi.menze.classes.dao.DateSaldoDao;
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
    TextView iconFa;
    KorisnikDao korisnikDao;
    LinearLayout layout;

    DateSaldoDao dateSaldoDao;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;


    private GraphicalView mChart;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_provjera_salda_main, container, false);
        setViews();
        setVisibility(false);
        upis.setText("601983");


        Typeface fontFamily = Typeface.createFromAsset(container.getResources().getAssets(), "fontawesome.ttf");
        iconFa.setTypeface(fontFamily);
        iconFa.setText("\uf0c9 ");

        korisnikDao = new KorisnikDao(container.getContext());
        dateSaldoDao = new DateSaldoDao(container.getContext());

        final Korisnik korisnk = korisnikDao.get(1l);

        if(korisnk != null && korisnk.getId_x() != 0){
            prikazIznosa.setText(String.valueOf(korisnk.getSaldo()));
            setVisibility(true);

            if(mChart == null){
                initChart();
                addSampleData(dateSaldoDao.listAll());
                mChart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, 0.3f);
                layout.addView(mChart);
            }else{
                //mChart.repaint();
                mChart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, 0.3f);
                layout.addView(mChart);
            }

        }

        setClickListeners();

        return view;
    }


    private void setClickListeners() {

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
                if(dateSaldoDao != null)
                    dateSaldoDao.deleteAll();
                if(alarmManager != null) {
                    alarmManager.cancel(alarmIntent);
                }
                mCurrentSeries.clear();
            }
        });
        iconFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) SaldoFragment.this.getActivity()).showMenu();
            }
        });
    }



    public void setViews() {
        provjeriButton = (Button) view.findViewById(R.id.provjeriButton);
        upis = (EditText) view.findViewById(R.id.editText);
        prikazIznosa = (TextView) view.findViewById(R.id.prikazIznosa);
        brojKartice = (TextView) view.findViewById(R.id.broj_kartice_tv);
        zapamtiMe = (CheckBox) view.findViewById(R.id.zapamtime_cb);
        zaboraviMe = (Button) view.findViewById(R.id.zaboraviMe);
        iconFa = (TextView) view.findViewById(R.id.icon_fa);
        layout = (LinearLayout) view.findViewById(R.id.chart);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent alertIntent = new Intent(getActivity(), SaldoReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, alertIntent, 0);
    }

    private void  setVisibility(boolean loggedIn){
        if(loggedIn){
            provjeriButton.setVisibility(View.GONE);
            upis.setVisibility(View.GONE);
            brojKartice.setVisibility(View.GONE);
            zapamtiMe.setVisibility(View.GONE);
            prikazIznosa.setVisibility(View.VISIBLE);
            zaboraviMe.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
        }else{
            brojKartice.setVisibility(View.VISIBLE);
            zapamtiMe.setVisibility(View.VISIBLE);
            provjeriButton.setVisibility(View.VISIBLE);
            upis.setVisibility(View.VISIBLE);
            prikazIznosa.setVisibility(View.INVISIBLE);
            zaboraviMe.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.GONE);


        }

   }

    private class SaldoCheck extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document doc = null;
            try {
                Log.d("poruka",strings[0]);
                doc = Jsoup.connect("http://www.cap.srce.hr/saldo.aspx?brk=" + strings[0]).timeout(0).get();
            } catch (IOException e) {
                return null;
            }
            String docText = doc.text();
            String beg = "Preostali saldo:";
            String res = docText.substring(docText.indexOf(beg) + beg.length(),
                    docText.indexOf("Status kartice:")).trim().replace(',','.');


            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            if(res == null){
                prikazIznosa.setText("Poteškoće s konekcijom");
                prikazIznosa.setVisibility(View.VISIBLE);
                return;
            }

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
                setAlarm();
            }

            prikazIznosa.setText(res);
            setVisibility(true);

        }
    }


    private void initChart() {
        mCurrentSeries = new XYSeries("Graf potrosnje");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.WHITE);
        mRenderer.setMarginsColor(Color.argb(96,228,241,254));
    }

    private void addSampleData(List<DateSaldo> mapaPotrosnje) {


        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("E dd.MM.yyyy");
        for(DateSaldo dateSaldo : mapaPotrosnje){
            i++;
            mCurrentSeries.add(i, dateSaldo.getSaldo());
            mCurrentSeries.addAnnotation(sdf.format(dateSaldo.getDate()), i, dateSaldo.getSaldo());
        }

    }

    public void setAlarm() {

        // Define a time value of 5 seconds
        Long alertTime = new GregorianCalendar().getTimeInMillis()+10*1000;

        // Define our intention of executing AlertReceiver

        //TODO change time
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 5 * 1000L,
                10*1000L, alarmIntent);


    }

}
