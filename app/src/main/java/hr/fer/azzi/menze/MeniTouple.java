package hr.fer.azzi.menze;

import java.util.List;

/**
 * Created by Azzaro on 15.1.2015..
 */
public class MeniTouple {
    private String title;
    private List<String> food;

    public MeniTouple(String title, List<String> food) {
        this.title = title;
        this.food = food;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getFood() {
        return food;
    }

    public void setFood(List<String> food) {
        this.food = food;
    }
}
