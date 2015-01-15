package hr.fer.azzi.menze;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Azzaro on 15.1.2015..
 */
public class SavskaParser {
    private static final String MLL = "MENU LINIJA LIJEVO:";
    private static final String ILL = "IZBOR  LINIJA LIJEVO:";
    private static final String PRILOZI = "PRILOZI:";
    private static final String MLD = "MENU LINIJA DESNO:";
    private static final String ILD = "IZBOR LINIJA DESNO:";
    private static final String VEGE = "VEGETARIJANSKI MENU:";


    private static final String VECERA = "VEČERA MENU:";
    private static final String IZBOR = "IZBOR:";
    private static final String PRILOZI_M = "Prilozi:";

    public  static List<MeniTouple> parseSavska(Element element){
        String elementString = element.text();

        List<MeniTouple> meniToupleList = new ArrayList<>();

        meniToupleList.add(new MeniTouple("Ručak", new ArrayList<String>()));
        String rucakLinijaLijevo = elementString.substring(elementString.indexOf(MLL) + MLL.length(), elementString.indexOf(ILL));
        meniToupleList.add(new MeniTouple("Menu linija lijevo", arrayToList(rucakLinijaLijevo.split(","))));
        String rucakIzbor = elementString.substring(elementString.indexOf(ILL) + ILL.length(), elementString.indexOf(PRILOZI));
        meniToupleList.add(new MeniTouple("Rucak izbor lijevo", arrayToList(rucakIzbor.split(","))));
        String prilozi = elementString.substring(elementString.indexOf(PRILOZI) + PRILOZI.length(), elementString.indexOf(MLD));
        meniToupleList.add(new MeniTouple("Prilozi lijevo", arrayToList(prilozi.split(","))));
        String menuLinijaDesno = elementString.substring(elementString.indexOf(MLD) + MLD.length(), elementString.indexOf(ILD));
        meniToupleList.add(new MeniTouple("Menu linija desno", arrayToList(menuLinijaDesno.split(","))));
        String izobrLinijaDesno = elementString.substring(elementString.indexOf(ILD) + ILD.length(),
                elementString.indexOf(PRILOZI,elementString.indexOf(ILD)));
        meniToupleList.add(new MeniTouple("Izbor linija desno", arrayToList(izobrLinijaDesno.split(","))));
        String prilozi2 = elementString.substring(elementString.indexOf(PRILOZI,elementString.indexOf(ILD)) + PRILOZI.length(),
                elementString.indexOf(VEGE));
        meniToupleList.add(new MeniTouple("Prilozi desno", arrayToList(prilozi2.split(","))));
        String vegeMenu = elementString.substring(elementString.indexOf(VEGE) + VEGE.length(),
                elementString.indexOf(VECERA));
        meniToupleList.add(new MeniTouple("Vegetarijanski menu", arrayToList(vegeMenu.split(","))));

        meniToupleList.add(new MeniTouple("Večera", new ArrayList<String>()));
        String veceraMenu = elementString.substring(elementString.indexOf(VECERA) + VECERA.length(),
                elementString.indexOf(VEGE,elementString.indexOf(VECERA) ));
        meniToupleList.add(new MeniTouple("Vecera menu", arrayToList(veceraMenu.split(","))));
        String vegeVecera = elementString.substring(elementString.indexOf(VEGE,elementString.indexOf(VECERA) ) + VEGE.length(),
                elementString.indexOf(IZBOR, elementString.indexOf(VECERA) ));
        meniToupleList.add(new MeniTouple("Vegetarijanski menu", arrayToList(vegeVecera.split(","))));

        String priloziVecera = elementString.substring(elementString.indexOf(PRILOZI_M) + PRILOZI_M.length(), elementString.indexOf("*Normativ"));
        meniToupleList.add(new MeniTouple("Prilozi vecera", arrayToList(priloziVecera.split(","))));
        return meniToupleList;

    }

    private static List<String> arrayToList(String... strings){
        List<String> list = new ArrayList<>();
        for (String s : strings){
            list.add(s);
        }
        return list;
    }
}
