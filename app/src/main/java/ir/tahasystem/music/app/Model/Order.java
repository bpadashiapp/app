package ir.tahasystem.music.app.Model;

import android.content.Context;

import java.util.List;
import java.util.regex.Pattern;

import ir.onlinefood.app.factory.R;
import ir.tahasystem.music.app.OrderActivity;

/**
 * Created by BabakPadashi on 6/7/2016.
 */
public class Order {

    static final long serialVersionUID =8996890340799609057L;

    public int orderId;
    public String username;
    public String fullname;
    public String phone;
    public String address;
    public double totalprice;
    public String dateTime;
    public boolean isAccept;
    public double discount;
    public double shippingCost;
    public String pesianShippingDate;
    public String ShippingTime;
    public int device;
    public String description;
    public double useSharjAmount;
    public double onlinePayAmount;
    public int ShippingTimeID;
    public int paymentTypeId;
    public boolean sendedByShipper;
    public double servicecharge;
    public double vat;

    public List<OrderDetails> lstOrderDetails;
    public String pesianDateTime;
    private String onlinePayStatus;

    public String getPhone() {
        return phone.split(Pattern.quote("_"))[0];
    }

    public String getMobile() {
        return phone.split(Pattern.quote("_"))[1];
    }

    public double getShippingCost() {

        if (shippingCost > 0)
            return shippingCost;
        return 0;
    }

    public String getStatus() {

        if ((int) (shippingCost) == -1)
            return OrderActivity.context.getString(R.string.wait_for_submit);
        else if ((int) (shippingCost) == 0)
            return OrderActivity.context.getString(R.string.not_success);

        return OrderActivity.context.getString(R.string.success);
    }

    public double getFinalPrice() {
        double finalPrice = (totalprice + vat) + getShippingCost();
        return finalPrice;
    }

    public String getPaymentType(Context context, Order aOrder) {
        if (paymentTypeId == 5)
            return context.getString(R.string.pay_online)+" "+aOrder.onlinePayStatus;
        return context.getString(R.string.in_place);
    }
}
