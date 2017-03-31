package ir.tahasystem.music.app.Model;

import java.util.List;

/**
 * Created by Onlinefood-5 on 5/30/2016.
 */
public class SaveOrder {

    static final long serialVersionUID =8996890340799609057L;

    public String username;
    public int companyId;
    public int paymentTypeId;
    public int  shippingTimeId;
    public String shippingDate;
    public int ShippingCost;
    public String description;
    public boolean usesharjAmount;
    public List<OrderParams> lst;

}
