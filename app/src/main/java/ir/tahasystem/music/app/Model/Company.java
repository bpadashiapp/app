package ir.tahasystem.music.app.Model;

import java.io.Serializable;

/**
 * Created by BabakPadashi on 6/9/2016.
 */
public class Company implements Serializable {

    static final long serialVersionUID =8996890340799609057L;

    public double Xposition;
    public double Yposition;
    public String companyName;
    public int companyId;
    public String address;

    public String shortDescription;
    public String fullDescription;
    public String img;
    public String phone;
    public String Mobile;
    public String messageDay;

    public int saleType;
    public boolean showPrice;
    public int AccessType;
    public boolean IsManufacture;
    public int takCatId;

    public boolean notification;
    public boolean basket;
    public boolean chat;
    public boolean gallery;
    public boolean mogheyat;
    public boolean tamasbaMa;
    public boolean tamasbaSazandeh;
    public boolean darbareMa;
    public boolean savabegh;
    public String msg;
}
