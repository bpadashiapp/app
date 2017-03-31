package ir.tahasystem.music.app.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ir.tahasystem.music.app.utils.Serialize;


public class ModelHolderService {


    // private ModelHolderService() {
    // }

    // private static class SingletonHelper {
    //private static final ModelHolderService INSTANCE = new ModelHolderService();
    // }

    //public static ModelHolderService getInstance() {
    // return SingletonHelper.INSTANCE;
    //}

    private static Object mutex = new Object();

    private static ModelHolderService aModelHolderService = null;

    private ModelHolderService() {
    }

    public static ModelHolderService getInstance() {
        if (aModelHolderService == null)
            synchronized (mutex) {
                if (aModelHolderService == null) {
                    aModelHolderService = new ModelHolderService();
                } else {
                    return aModelHolderService;
                }

            }
        return aModelHolderService;
    }


    public List<Kala> getKalaList(Context context) {

        return (List<Kala>) new Serialize().readFromFile(context,
                "aKalaListNotify");

    }

    public void setKalaList(Context context, List<Kala> aKalaList) {

        new Serialize().saveToFile(context, aKalaList,
                "aKalaListNotify");
    }

    public Kala getKalaOrder(Context context) {

        return (Kala) new Serialize().readFromFile(context,
                "aKalaListNotifyOrder");

    }

    public void setKalaOrder(Context context, Kala aKala) {

        new Serialize().saveToFile(context, aKala,
                "aKalaListNotifyOrder");
    }


    public void setKalaListChat(Context context, Kala aKala) {

        List<Kala> aListKala = (List<Kala>) new Serialize().readFromFile(context,
                "aKalaListChat");

        if(aListKala==null)
            aListKala=new ArrayList<>();

        aListKala.add( aKala);

        new Serialize().saveToFile(context, aListKala,
                "aKalaListChat");

    }

    public void setKalaListChat(Context context, List<Kala> aListKala) {


        new Serialize().saveToFile(context, aListKala,
                "aKalaListChat");

    }

    public List<Kala> getKalaListChat(Context context) {

        return (List<Kala>) new Serialize().readFromFile(context,
                "aKalaListChat");

    }

    //

    public void setKalaListChatManager(Context context, Kala aKala) {

        List<Kala> aListKala = (List<Kala>) new Serialize().readFromFile(context,
                "aKalaListChatManager");

        if(aListKala==null)
            aListKala=new ArrayList<>();

        aListKala.add( aKala);

        new Serialize().saveToFile(context, aListKala,
                "aKalaListChatManager");

    }

    public void setKalaListChatManager(Context context, List<Kala> aListKala) {


        new Serialize().saveToFile(context, aListKala,
                "aKalaListChatManager");

    }

    public List<Kala> getKalaListChatManager(Context context) {

        return (List<Kala>) new Serialize().readFromFile(context,
                "aKalaListChatManager");

    }



}
