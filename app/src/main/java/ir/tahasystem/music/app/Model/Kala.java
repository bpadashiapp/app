package ir.tahasystem.music.app.Model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Kala implements Serializable {
    static final long serialVersionUID =8996890340799609057L;
    public String type;
    public String name;
    public String dir;
    public long price;
    public long priceByDiscount;

    public long initprice;
    public long initpriceByDiscount;

    public int isServer;
    public int numServer;


    public String description;
    public List<mediasRoot> product_has_media;

    public int id;
    public boolean has_child;
    public String image;
    public String cat_id;
    public String number_sale;
    public float discount;
    public String offer_type;
    public String maplat;
    public String maplang;
    public String store_name;
    public String store_address;
    public String sharayet;
    public String store_tel;
    public String joziat;
    public int unitsInStock;
    public int minOrder;

    // # Copon
    public boolean check;
    public boolean active;
    public String title;
    public String exp_date;
    public String bdate;
    public String avatar;
    public String status;
    public String numb;
    public String pay;
    public String date;
    public String use_date;

    // #timer
    public int ryear;
    public int rmonth;
    public int rday;
    public int rhour;
    public int rminute;
    public int rsec;

    public String number;

    public String shegeft;
    public String kamyab;
    public String code;
    public long rem;

    // #market
    public String number_view;
    public int unUsedNum;
    public String usedNumSold;
    public String usedAdmSold;
    public String endDate;
    public String startDate;
    public int rate;


    // #message
    public String parrent_id;
    public String creator;
    public StringBuilder parent_texts;

    // #notify
    public boolean isView;
    public String offer;
    public String copon;

    public List<CodesRoot> codes;
    public String text;

    public int statusValue;
    public String color;
    public int itemType;

    // #compartor
    public String basketStatus;
    public int priceByDiscountNew;


    // #CoponCancel
    public boolean coponIsValid;
    public boolean checked;
    public int rank;
    public long count;

    public int orderId;
    public long limt;
    public String saleType;
    public String tag;
    public boolean isOrder;
    public boolean isOrderSeen;
    public int neworder;

    public String priceDetail;
    public int categoryId;

    //#ONLINE FOOD

    public int companyId;
    public String companyName;
    public String address;
    public int maincategoryId;
    public String shortDescription;
    public String fullDescription;
    public String distance;
    public boolean isReady;
    public String messageDay;
    public String img;

    public boolean isFave;
    public boolean isDelete;
    public String vat;
    public String serviceCharge;

    public String Xposition;
    public String Yposition;


    //#------
    public String uid;
    public String command;
    public String sender;
    public String receiver;
    public int isRec;
    public boolean isSelected;
    public String username;
    public int appType;
    public int DeviceId;



    public Kala() {
        this.name = name;
        this.id = id;
        this.priceByDiscount = priceByDiscount;
    }

    public String getRem() {

        SimpleDateFormat formatter = new SimpleDateFormat(" hh:mm:ss ساعت dd روز  MM ماه ");
        return formatter.format(rem);

    }

    public String getUseDate() {
        if (use_date == null)
            use_date = " - ";
        return use_date;
    }

    public String getNumb() {
        if (numb == null)
            numb = " - ";
        return numb;
    }

    public String getCode() {
        if (code == null)
            code = " - ";
        return code;
    }

    public class CodesRoot implements Serializable {

        public Code code;

    }

    public class Code implements Serializable {

        public String code;
        public String number;

    }

    public class mediasRoot implements Serializable {

        public Medias medias;

    }

    public class Medias implements Serializable {

        public String name;
        public String thumb;

    }

    public String getDescription() {

        if (description != null && description.length() > 27)
            return description.substring(0, 27) + "..";
        else if (description == null)
            description = " - ";

        return description;

    }

    public String getName() {

        if (name != null && name.length() > 60)
            return name.substring(0, 60) + "..";
        else if (name == null)
            name = " - ";

        return name;

    }

    public String getDescriptionKala() {

        if (description != null && description.length() > 380)
            return description.substring(0, 380) + "..";
        else if (description == null)
            description = " - ";

        return description;

    }

    public String getNumberSale() {
        return number_sale;
    }

    public String getPrice() {

        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");
        return df.format(price);

    }

    public String getPriceByDiscount() {


        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");
        return df.format(priceByDiscount);

    }

    public String initgetPrice() {

        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");
        return df.format(initprice);

    }

    public String initgetPriceByDiscount() {


        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");
        return df.format(initpriceByDiscount);

    }

    public long getPriceByDiscountValue() {



        return priceByDiscount;

    }

    public String getTitle() {
        if (title != null && title.length() > 90)
            return title.substring(0, 90) + "..";
        else if (title == null)
            title = " - ";

        return title;
    }

}
