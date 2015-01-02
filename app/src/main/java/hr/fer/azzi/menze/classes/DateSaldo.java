package hr.fer.azzi.menze.classes;

import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

import java.util.Date;

import javax.persistence.GeneratedValue;

/**
 * Created by Azzaro on 29.12.2014..
 */
@Entity
public class DateSaldo {

    @Id
    @GeneratedValue
    private long id;
    private Date date;
    private double saldo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
