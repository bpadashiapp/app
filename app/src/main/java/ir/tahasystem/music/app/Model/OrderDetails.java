package ir.tahasystem.music.app.Model;

/**
 * Created by BabakPadashi on 6/7/2016.
 */
public class OrderDetails {

    static final long serialVersionUID =8996890340799609057L;

    public int orderId;
    public String barcode;
    public double quantity;
    public String description;
    public double price;
    public double discount;
    public String saleType;
    public long priceByDiscount;
    public double discountPrice;
}
