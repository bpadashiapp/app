package ir.tahasystem.music.app.Model;

import android.widget.CheckBox;

import java.io.Serializable;

public class BasketModel implements Serializable {

    static final long serialVersionUID =8996890340799609057L;

    public Kala aKala;
    public long count;

    public transient CheckBox aCheckBoxName;
    public transient CheckBox aCheckBoxSelectItem;



    public BasketModel(Kala aKala) {
        this.aKala = aKala;
    }

    public BasketModel() {
    }


}
