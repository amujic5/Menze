package hr.fer.azzi.menze.classes;


import android.content.Context;

import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;



/**
 * Created by Azzaro on 24.12.2014..
 */
@Entity
public class Korisnik {

    @Id
    private long id;
    private long id_x;
    private double saldo;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_x() {
        return id_x;
    }

    public void setId_x(long id_x) {
        this.id_x = id_x;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
