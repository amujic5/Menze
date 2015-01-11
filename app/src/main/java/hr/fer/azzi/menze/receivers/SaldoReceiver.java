package hr.fer.azzi.menze.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import hr.fer.azzi.menze.R;
import hr.fer.azzi.menze.classes.DateSaldo;
import hr.fer.azzi.menze.classes.Korisnik;


/**
 * Created by Azzaro on 1.1.2015..
 */
public class SaldoReceiver extends BroadcastReceiver{


    Korisnik korisnik;
    Context context;

    public static final int MAX_SALDO_HISTORY = 30;
    private static final int MIN_SALDO_NOTIFIC = 50;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("poceo reciver","poceo reciver");

        this.context = context;
        if (intent != null &&
                intent.getAction() != null &&
                intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if(korisnik != null && korisnik.getId_x() != 0){
                setAlarm();
            }
            return;
        }


    }

    private void setAlarm() {
        AlarmManager alarmManager;
        PendingIntent alarmIntent;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alertIntent = new Intent(context, SaldoReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, alertIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY,
        AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    private class getSaldo extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... voids) {
            Document doc;
            try {
                doc = Jsoup.connect("http://www.cap.srce.hr/saldo.aspx?brk=" + korisnik.getId_x()).timeout(0).get();
                String docText = doc.text();
                String beg = "Preostali saldo:";

                return docText.substring(docText.indexOf(beg) + beg.length(),
                        docText.indexOf("Status kartice:")).trim().replace(',','.');
            } catch (IOException e) {
            }
                return null;
        }

        @Override
        protected void onPostExecute(String res) {
            if (res == null)
                return;
            Log.d("dohvaceno","dohvacenoooo");
            double saldoDobule = Double.parseDouble(res);
            korisnik.setSaldo(saldoDobule);
            DateSaldo dateSaldo = new DateSaldo();
            dateSaldo.setSaldo(saldoDobule);
            dateSaldo.setDate(new Date());



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

        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
