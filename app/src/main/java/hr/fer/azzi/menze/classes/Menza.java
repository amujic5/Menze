package hr.fer.azzi.menze.classes;

import java.io.Serializable;

/**
 * Created by Azzaro on 17.12.2014..
 */
public class Menza implements Serializable{
    private String naziv;
    private String link;


    private int idSlike;
    private int idOpis;


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIdOpis() {
        return idOpis;
    }

    public void setIdOpis(int idOpis) {
        this.idOpis = idOpis;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


    public int getIdSlike() {
        return idSlike;
    }

    public void setIdSlike(int idSlike) {
        this.idSlike = idSlike;
    }
}
