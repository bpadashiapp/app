package ir.tahasystem.music.app.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BabakPadashi on 6/13/2016.
 */
public class MapModel implements Serializable {
    static final long serialVersionUID =8446890340799609057L;


    public List<List<HashMap<String, String>>> result;

    public double latFactroy;
    public double lngFactroy;

    public double latHome;
    public double lngHome;

    public String address;

}
