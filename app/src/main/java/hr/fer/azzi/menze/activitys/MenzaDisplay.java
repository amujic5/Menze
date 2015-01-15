package hr.fer.azzi.menze.activitys;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import hr.fer.azzi.menze.MeniTouple;
import hr.fer.azzi.menze.R;
import hr.fer.azzi.menze.SavskaParser;
import hr.fer.azzi.menze.adapters.ExpandableListAdapter;
import hr.fer.azzi.menze.classes.Menza;

/**
 * Created by Azzaro on 18.12.2014..
 */
public class MenzaDisplay extends Activity {

    TextView title;
    ImageView image;
    ExpandableListView description;
    Menza menza;

    ExpandableListAdapter listAdapter;
    HashMap<String, List<String>> listDataChild;
    List<String> listDataHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        for (String redakOpisa : poljeOpisa) {
            listaOpisa.add(redakOpisa);
        }


        listDataHeader.add("Radno vrijeme");

        listDataChild = new HashMap<>();
        listDataChild.put(listDataHeader.get(0), listaOpisa);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        description.setAdapter(listAdapter);


        if (menza.getLink() != null) {
            if(menza.getNaziv().equals("Savska"))
                new HtmlGetSavskaParser().execute(menza.getLink());
            else
                new HtmlGetParser().execute(menza.getLink());

        }

    }


    private void setViews() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        title = (TextView) findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.image);
        description = (ExpandableListView) findViewById(R.id.description);
    }

    private class HtmlGetSavskaParser extends AsyncTask<String, Void, Element>{

        private String resDate;
        @Override
        protected Element doInBackground(String... strings) {
            Element element = null;
            try {
                Document doc = Jsoup.connect(strings[0]).timeout(0).get();

                String docText = doc.text();
                String beg = "Zadnja izmjena: ";
                 resDate = docText.substring(docText.indexOf(beg, docText.indexOf(beg) + beg.length()) + beg.length(),
                        docText.indexOf("View")).trim();

                element = doc.getElementsByClass("newsItem").get(1).getElementsByClass("content").get(0);


            } catch (Exception e) {
                Log.d("test", "exception" + e.toString());
            }

            return element;
        }

        @Override
        protected void onPostExecute(Element element) {
            if (element == null)
                return;
            List<MeniTouple> listMeniTouple = SavskaParser.parseSavska(element);
            listAdapter.addElement(resDate, new ArrayList<String>());
            for(MeniTouple meniTouple : listMeniTouple){
                listAdapter.addElement(meniTouple.getTitle(), meniTouple.getFood());
            }

        }
    }

    private class HtmlGetParser extends AsyncTask<String, Void, List<String>> {


        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> redovi = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(strings[0]).timeout(0).get();
                String docText = doc.text();

                String beg = "Zadnja izmjena: ";
                String res = docText.substring(docText.indexOf(beg, docText.indexOf(beg) + beg.length()) + beg.length(),
                        docText.indexOf("View")).trim();
                redovi.add(res);

                Element element = doc.getElementsByClass("newsItem").get(1).getElementsByClass("content").get(0);

                for (Element elementChild : element.children()) {

                    String line = elementChild.text();
                    if (line.replaceAll("\\s+", "").replaceAll("\\p{C}", "").trim().length() < 2 && redovi.get(redovi.size() - 1).length() < 2)
                        continue;

                    if (line.contains(":") && line.split(":").length > 1 && line.split(":")[1].length() > 15) {
                        String[] jela = line.split(":")[1].split(",");
                        redovi.add("\t" + line.split(":")[0].trim());
                        for (String jelo : jela) {
                            if (jelo.trim().length() < 3)
                                continue;
                            redovi.add("\t\t\u2022 " + jelo.trim());
                        }
                        continue;
                    }
                    redovi.add(line.trim());
                }
            } catch (Exception e) {
                Log.d("test", "exception" + e.toString());
            }

            return redovi;
        }

        @Override
        protected void onPostExecute(List<String> lineList) {
            if (lineList.isEmpty())
                return;
            String date = lineList.get(0);
            lineList.remove(0);
            listAdapter.addElement("Meni " + date, lineList);
        }
    }
}
