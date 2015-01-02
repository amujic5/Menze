package hr.fer.azzi.menze;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import hr.fer.azzi.menze.classes.DateSaldo;
import hr.fer.azzi.menze.classes.Korisnik;
import hr.fer.azzi.menze.classes.dao.DateSaldoDao;
import hr.fer.azzi.menze.classes.dao.KorisnikDao;

/**
 * Created by Azzaro on 1.1.2015..
 */
public class SaldoReceiver extends BroadcastReceiver{

    DateSaldoDao dateSaldoDao;
    KorisnikDao korisnikDao;
    Korisnik korisnik;
    Context context;

    public static final int MAX_SALDO_HISTORY = 30;
    private static final int MIN_SALDO_NOTIFIC = 50;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("poceo reciver","poceo reciver");
        this.context = context;
        korisnikDao = new KorisnikDao(context);
        korisnik = korisnikDao.get(1l);

        if(korisnik != null && korisnik.getId_x() != 0){
            dateSaldoDao = new DateSaldoDao(context);
            new getSaldo().execute();
        }
    }

    private class getSaldo extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.cap.srce.hr/saldo.aspx?brk=" + korisnik.getId_x()).get();
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
            Log.d("dohvaceno","dohvacenoooo");
            double saldoDobule = Double.parseDouble(res);
            korisnik.setSaldo(saldoDobule);
            DateSaldo dateSaldo = new DateSaldo();
            dateSaldo.setSaldo(saldoDobule);
            dateSaldo.setDate(new Date());
            dateSaldoDao.insert(dateSaldo);

            List<DateSaldo> listDateSaldo = dateSaldoDao.listAll();
            if(listDateSaldo.size() > MAX_SALDO_HISTORY){
                dateSaldoDao.delete(listDateSaldo.get(0).getId());
            }

            if(saldoDobule < MIN_SALDO_NOTIFIC)
                createNotification(res);
        }
    }

    private void createNotification(String res) {

        // Builds a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.money)
                        .setContentTitle("Niski iznos salda")
                        .setTicker("Saldo obavijest")
                        .setContentText("trenutni saldo: " + res);

        // Defines the Intent to fire when the notification is clicked
       // mBuilder.setContentIntent(notificIntent);

        // Set the default notification option
        // DEFAULT_SOUND : Make sound
        // DEFAULT_VIBRATE : Vibrate
        // DEFAULT_LIGHTS : Use the default light notification
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        // Auto cancels the notification when clicked on in the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        mNotificationManager.notify(1, mBuilder.build());
    }
}
