package hr.fer.azzi.menze.classes;

import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

import java.io.Serializable;

/**
 * Created by Azzaro on 17.12.2014..
 */

public class Menza implements Serializable{

    private String naziv;
    private String link;
    private String ulica;
    private int idSlike;
    private int idOpis;

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

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
