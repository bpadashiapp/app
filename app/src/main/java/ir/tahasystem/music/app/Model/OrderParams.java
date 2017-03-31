package ir.tahasystem.music.app.Model;

/**
 * Created by Onlinefood-5 on 5/30/2016.
 */
public class OrderParams {

    static final long serialVersionUID =8996890340799609057L;

    public int PId;
    public long quantity;

    public OrderParams(int id, long count) {

        PId=id;
        quantity=count;
    }
}
