package ir.tahasystem.music.app.Model;

import android.content.Context;

import ir.tahasystem.music.app.utils.Serialize;


public class LoginHolder {

    LoginModel aLoginModel = null;


    private static Object mutex = new Object();

    private static LoginHolder aModelHolder = null;
    private Context context;

   private LoginHolder() {
    }

    public static LoginHolder getInstance() {
        if (aModelHolder == null)
            synchronized (mutex) {
                if (aModelHolder == null) {
                    aModelHolder = new LoginHolder();
                } else {
                    return aModelHolder;
                }

            }
        return aModelHolder;
    }


   // private LoginHolder() {
    //}

    private static class SingletonHelper {
        private static final LoginHolder INSTANCE = new LoginHolder();
    }

    //public static LoginHolder getInstance() {
      //  return SingletonHelper.INSTANCE;
    //}

    public void init(Context context) {

        this.context = context;

    }

    public void setLogin(LoginModel aLoginModel) {

        this.aLoginModel = aLoginModel;
        new Serialize().saveToFile(context, aLoginModel, "aLoginModel");

        System.out.println("-->>>"+(LoginModel) new Serialize().readFromFile(context,
                "aLoginModel"));
    }

    public LoginModel getLogin() {

        if (aLoginModel != null)
            return aLoginModel;

        aLoginModel = (LoginModel) new Serialize().readFromFile(context,
                "aLoginModel");


        return aLoginModel;
    }


    public String getLoginId() {

        if (aLoginModel != null)
            return aLoginModel.uid;

        aLoginModel = (LoginModel) new Serialize().readFromFile(context,
                "aLoginModel");

        if (aLoginModel != null)
            return aLoginModel.uid;


        return null;
    }
}
