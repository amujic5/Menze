package hr.fer.azzi.menze;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import hr.fer.azzi.menze.classes.Menza;

/**
 * Created by Azzaro on 18.12.2014..
 */
public class MenzaDisplay extends Activity {

    TextView title;
    ImageView image;
    ListView description;
    Menza menza;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            menza = (Menza) extras.get("menza");
        }

        setViews();

        title.setText(menza.getNaziv());
        image.setImageResource(menza.getIdSlike());
        String[] poljeOpisa = getResources().getStringArray(menza.getIdOpis());
        List<String> listaOpisa = new ArrayList<>();
        for(String redakOpisa : poljeOpisa){
            listaOpisa.add(redakOpisa);
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaOpisa);
        description.setAdapter(arrayAdapter);

        new HtmlGetParser().execute(menza.getLink());
    }

    private void setViews() {
        title = (TextView) findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.image);
        description = (ListView) findViewById(R.id.description);
    }

    private class HtmlGetParser extends AsyncTask<String, Void, List<String>> {


        @Override
        protected List<String> doInBackground(String...  strings) {
            List<String> redovi = new ArrayList<>();
            redovi.add("");
            try{
                Document doc = Jsoup.connect(strings[0]).get();
                Element element = doc.getElementsByClass("newsItem").get(1).getElementsByClass("content").get(0);

                for(Element elementChild : element.children()){
                  //  if(!redovi.get(redovi.size() - 1).isEmpty()){
                       // redovi.add("");}

                    String line = elementChild.text();
                  //  if(line.replace("\\s+","").length() == 1) continue;

                    if(line.contains(":") && line.split(":").length > 1 && line.split(":")[1].length() > 15){
                        String[] jela = line.split(":")[1].split(",");
                        redovi.add("\t" + line.split(":")[0].trim());
                        for(String jelo : jela){
                            redovi.add("\t\t\u2022 " + jelo.trim());
                        }
                        continue;
                    }

                    redovi.add(line.trim());
                }
            } catch (Exception e){
                Log.d("test", "exception" + e.toString());
            }

            return redovi;
        }

        @Override
        protected void onPostExecute(List<String> lineList) {
            for (String line: lineList){
                arrayAdapter.add(line);
            }
        }
    }
}