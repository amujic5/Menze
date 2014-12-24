package hr.fer.azzi.menze;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hr.fer.azzi.menze.classes.Menza;

/**
 * Created by Azzaro on 18.12.2014..
 */
public class MenzaDisplay extends Activity {

    TextView title;
    ImageView image;
    ExpandableListView description;
    Menza menza;
    ArrayAdapter<String> arrayAdapter;

    ExpandableListAdapter listAdapter;
    HashMap<String, List<String>> listDataChild;
    List<String> listDataHeader;

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


        //listDataHeader.add("Radno vrijeme");

        //listDataChild = new HashMap<>();
        //listDataChild.put(listDataHeader.get(0),listaOpisa);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        description.setAdapter(listAdapter);

        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaOpisa);
       // description.setAdapter(arrayAdapter);

        if(menza.getLink() != null){
            //new HtmlGetParser().execute(menza.getLink());
        }

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    private void setViews() {
        title = (TextView) findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.image);
        description = (ExpandableListView) findViewById(R.id.description);
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
