package ir.tahasystem.music.app.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ir.tahasystem.music.app.utils.Serialize;


public class ModelHolder {

    LoginModel aLoginModel = null;
    LoginModel aLoginModelMarket = null;

    public int ShippingTimeId;
    public String ShippingDate;
    public int paymentTypeId = 5;
    public int ShippingCost = -2;
    public String description;

    List<BasketModel> aBasketModelList = new ArrayList<BasketModel>();
    private Context context;
    public int bagValue;


    public String service;
    public String vat;
    public String ShippingCostDes;


    List<BasketModel> aBasketModelListBuying = new ArrayList<BasketModel>();


    private static Object mutex = new Object();

    private static ModelHolder aModelHolder = null;
    public int delivery;
    public boolean dontPay;
    public String systemSharjAmount;
    public int saleType;


    private ModelHolder() {
    }

    public static ModelHolder getInstance() {
        if (aModelHolder == null)
            synchronized (mutex) {
                if (aModelHolder == null) {
                    aModelHolder = new ModelHolder();
                } else {
                    return aModelHolder;
                }

            }
        return aModelHolder;
    }


    //private ModelHolder() {
    //}

    //private static class SingletonHelper {
    //private static final ModelHolder INSTANCE = new ModelHolder();
    // }

    // public static ModelHolder getInstance() {
    // return SingletonHelper.INSTANCE;
    //}


    public void init(Context context) {

        this.context = context;

        List<BasketModel> aBasketModelList = (List<BasketModel>) new Serialize().readFromFile(context,
                "aBasketModelList");

        if (aBasketModelList != null)
            this.aBasketModelList = aBasketModelList;

    }

    public void addBasket(Kala aKala) {
        BasketModel aBasketModel = new BasketModel();
        aBasketModel.aKala = aKala;
        aBasketModel.count = 0;
        this.aBasketModelList.add(0, aBasketModel);
        new Serialize().saveToFile(context, this.aBasketModelList, "aBasketModelList");
    }

    public void addBasket(BasketModel aBasketModel) {
        this.aBasketModelList.add(0, aBasketModel);
        new Serialize().saveToFile(context, this.aBasketModelList, "aBasketModelList");
    }

    public void resetBasket(List<BasketModel> aBasketModelList) {
        this.aBasketModelList = aBasketModelList;
        new Serialize().saveToFile(context, this.aBasketModelList, "aBasketModelList");
    }

    public List<BasketModel> getBasketSelected() {
        List<BasketModel> eBasketModelList = new ArrayList<BasketModel>();
        for (BasketModel eBasketModel : aBasketModelList)
            if (eBasketModel.aCheckBoxName.isChecked())
                eBasketModelList.add(eBasketModel);
        return eBasketModelList;
    }

    public List<BasketModel> getBasket() {
        return aBasketModelList;
    }

    public BasketModel getBasket(int count) {
        return aBasketModelList.get(count);
    }

    public int getBasketCount() {
        return aBasketModelList.size();
    }

    public void removeBasket(BasketModel aBasketModel) {
        this.aBasketModelList.remove(aBasketModel);
        new Serialize().saveToFile(context, this.aBasketModelList, "aBasketModelList");
    }

    public void clearBasket() {
        this.aBasketModelList.clear();
        new Serialize().saveToFile(context, this.aBasketModelList, "aBasketModelList");
    }

    public void addBasketBuying(BasketModel aBasketModel) {
        aBasketModelListBuying.add(aBasketModel);
    }

    public void addBasketBuyingAll() {
        for (BasketModel bBasketModel : aBasketModelList)
            if (bBasketModel.aCheckBoxName.isChecked())
                aBasketModelListBuying.add(0, bBasketModel);

    }

    public void buyDone() {

        for (BasketModel bBasketModel : aBasketModelListBuying)
            this.aBasketModelList.remove(bBasketModel);

        new Serialize().saveToFile(context, this.aBasketModelList, "aBasketModelList");

    }

    public void clearBuying() {
        aBasketModelListBuying.clear();
    }

    public List<OrderParams> getOrderParams() {

        List<OrderParams> aOrderParamsList=new ArrayList<OrderParams>();

        for (BasketModel bBasketModel : aBasketModelList)
            aOrderParamsList.add(new OrderParams(bBasketModel.aKala.id,bBasketModel.count));

        return aOrderParamsList;

    }


    public void removeBasketAll() {

        this.aBasketModelList.clear();
        new Serialize().saveToFile(context, this.aBasketModelList, "aBasketModelList");
    }
}
