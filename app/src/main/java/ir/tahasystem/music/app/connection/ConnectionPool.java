package ir.tahasystem.music.app.connection;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ir.tahasystem.music.app.Model.BasketModel;
import ir.tahasystem.music.app.Model.LoginHolder;
import ir.tahasystem.music.app.Model.LoginModel;
import ir.tahasystem.music.app.Model.ObjProduct;
import ir.tahasystem.music.app.Model.SaveOrder;
import ir.tahasystem.music.app.Values;

/**
 * Created by OnlinePakhsh-5 on 5/18/2016.
 */
public class ConnectionPool {

    public SoapObject ConnectGetCates(int company) throws IOException, XmlPullParserException {

        System.out.println("GetCategories, " + company + ",");

        String SOAP_ACTION = "http://tempuri.org/GetCategories";
        String METHOD_NAME = "GetCategories";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", company);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;

    }

    public SoapObject ConnectGetSubCates(int company, int cates) throws IOException, XmlPullParserException {

        System.out.println("GetSubCategories," + company + "," + cates);

        String SOAP_ACTION = "http://tempuri.org/GetSubCategories";
        String METHOD_NAME = "GetSubCategories";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", company);
        request.addProperty("categoryid", cates);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;

    }

    public SoapObject ConnectGetProduct(int companyId, int subCateId) throws IOException, XmlPullParserException {

        System.out.println("GetProducts," + companyId + "," + subCateId);

        String SOAP_ACTION = "http://tempuri.org/GetProducts";
        String METHOD_NAME = "GetProducts";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("subcategoryid", subCateId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetProductHome(int companyId, int page, int limit) throws IOException, XmlPullParserException {

        System.out.println("GetProductsSearch," + companyId + "," + page + "," + limit);

        String SOAP_ACTION = "http://tempuri.org/GetProductsSearch";
        String METHOD_NAME = "GetProductsSearch";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("productName", "");
        request.addProperty("pageindex", page);
        request.addProperty("pagesize", limit);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetProductSearch(int companyId, String search, int page, int limit) throws IOException, XmlPullParserException {
        System.out.println("GetProductsSearch," + companyId + "," + page + "," + limit + "," + search);

        String SOAP_ACTION = "http://tempuri.org/GetProductsSearch";
        String METHOD_NAME = "GetProductsSearch";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("productName", search);
        request.addProperty("pageindex", page);
        request.addProperty("categoryId", 0);
        request.addProperty("pagesize", limit);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetCate(int companyId) throws IOException, XmlPullParserException {

        System.out.println("GetCategoriesWithSubCategories," + companyId);

        String SOAP_ACTION = "http://tempuri.org/GetCategoriesWithSubCategories";
        String METHOD_NAME = "GetCategoriesWithSubCategories";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectVerifyMobile(String mobile, int activeBy) throws IOException, XmlPullParserException {

        System.out.println("VerifyMobile," + mobile);

        String SOAP_ACTION = "http://tempuri.org/VerifyMobileSpecial";
        String METHOD_NAME = "VerifyMobileSpecial";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("mobile", mobile);
        request.addProperty("introduserUsername", "");
        request.addProperty("CallOrSms", activeBy);
        request.addProperty("companyId", Values.companyId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);
        SoapObject response = null;
        ht.call(SOAP_ACTION, envelope);
        response = (SoapObject) envelope.getResponse();

        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectRegisterByMob(String code, String moaref) throws IOException, XmlPullParserException {

        System.out.println("RegisterByMob," + code);

        String SOAP_ACTION = "http://tempuri.org/RegisterByMob";
        String METHOD_NAME = "RegisterByMob";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", code);
        request.addProperty("introduserUsername", moaref);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetShippedTimesWeeks(int companyId,int year,int month) throws IOException, XmlPullParserException {

        System.out.println("RegisterByMob," + companyId+" YEAR "+year+" MON "+month);

        String SOAP_ACTION = "http://tempuri.org/GetShippedTimesWeeks";
        String METHOD_NAME = "GetShippedTimesWeeks";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("year", year);
        request.addProperty("month", month);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetShippedTimes(int companyId, int id,String tag) throws IOException, XmlPullParserException {

        System.out.println("ConnectGetShippedTimes," + companyId + "," + id+" TAG "+tag);

        String SOAP_ACTION = "http://tempuri.org/GetShippedTimes";
        String METHOD_NAME = "GetShippedTimes";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("OnlyIsAvilable", false);
        request.addProperty("day", id);
        request.addProperty("companyId", companyId);
        request.addProperty("ActiveToday", true);
        request.addProperty("strDate", tag);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetShippingCostOfDistance(int companyId, double lat, double lon) throws IOException, XmlPullParserException {

        System.out.println("ConnectGetShippingCostOfDistance, " + companyId + ", " + lat + ", " + lon);

        String SOAP_ACTION = "http://tempuri.org/GetShippingCostOfDistance";
        String METHOD_NAME = "GetShippingCostOfDistance";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("lat", String.valueOf(lat));
        request.addProperty("lon", String.valueOf(lon));
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetServiceCharge(int companyId) throws IOException, XmlPullParserException {

        System.out.println("ConnectGetServiceCharge, " + companyId);


        String SOAP_ACTION = "http://tempuri.org/GetServiceCharge";
        String METHOD_NAME = "GetServiceCharge";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetVat(int companyId) throws IOException, XmlPullParserException {

        System.out.println("ConnectGetVat, " + companyId);


        String SOAP_ACTION = "http://tempuri.org/GetVat";
        String METHOD_NAME = "GetVat";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetSharjAmount(int companyId, String username) throws IOException, XmlPullParserException {

        System.out.println("ConnectGetSharjAmount, " + companyId);


        String SOAP_ACTION = "http://tempuri.org/GetSharjAmount";
        String METHOD_NAME = "GetSharjAmount";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("username", username);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectSaveOrder(SaveOrder aSaveOrder) throws IOException, XmlPullParserException {

        System.out.println("SaveOrderJson, " + new Gson().toJson(aSaveOrder));


        String SOAP_ACTION = "http://tempuri.org/SaveOrderJson";
        String METHOD_NAME = "SaveOrderJson";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        // PropertyInfo pi=new PropertyInfo();
        //  pi.setName("jsonobject");
        // pi.setValue(jsonInString);
        // pi.setType(String.class);
        // request.addProperty(pi);
        request.addProperty("username", aSaveOrder.username);
        request.addProperty("companyId", aSaveOrder.companyId);
        request.addProperty("paymentTypeId", aSaveOrder.paymentTypeId);
        request.addProperty("shippingTimeId", aSaveOrder.shippingTimeId);
        request.addProperty("shippingDate", aSaveOrder.shippingDate);
        //request.addProperty("ShippingCost",aSaveOrder.ShippingCost );
        PropertyInfo pi = new PropertyInfo();
        pi.setName("ShippingCost");
        pi.setValue(aSaveOrder.ShippingCost);
        pi.setType(Double.class);
        request.addProperty(pi);

        request.addProperty("description", aSaveOrder.description);
        request.addProperty("usesharjAmount", aSaveOrder.usesharjAmount);
        //request.addProperty("lst",new Gson().toJson(aSaveOrder.lst));

        pi = new PropertyInfo();
        pi.setName("lst");
        pi.setValue(new Gson().toJson(aSaveOrder.lst).toString());
        pi.setType(String.class);
        request.addProperty(pi);

        /*
              <ns1:username>?</ns1:username>
      <ns1:companyId>0</ns1:companyId>
      <ns1:paymentTypeId>0</ns1:paymentTypeId>
      <ns1:shippingTimeId>0</ns1:shippingTimeId>
      <ns1:shippingDate>?</ns1:shippingDate>
      <ns1:ShippingCost>?</ns1:ShippingCost>
      <ns1:description>?</ns1:description>
      <ns1:usesharjAmount>true</ns1:usesharjAmount>
         */

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetInfo(String username) throws IOException, XmlPullParserException {

        System.out.println("ConnectGetInfo, " + username);

        String SOAP_ACTION = "http://tempuri.org/GetInfo";
        String METHOD_NAME = "GetInfo";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", username);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectUpdateInfo(String jsonInString) throws IOException, XmlPullParserException {

        System.out.println("ConnectUpdateInfo" + jsonInString);

        String SOAP_ACTION = "http://tempuri.org/UpdateInfoJson";
        String METHOD_NAME = "UpdateInfoJson";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("info");
        pi.setValue(jsonInString);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectIsManufacture(String jsonInString) throws IOException, XmlPullParserException {

        System.out.println("IsManufacture" + jsonInString);

        String SOAP_ACTION = "http://tempuri.org/IsManufacture";
        String METHOD_NAME = "IsManufacture";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("companyId", Values.companyId);
        request.addProperty("username", jsonInString);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectLogin(String username, String password) throws IOException, XmlPullParserException {
        System.out.println("ConnectUpdateInfo, " + username + " , " + password);

        String SOAP_ACTION = "http://tempuri.org/login";
        String METHOD_NAME = "login";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("companyId", Values.companyId);
        request.addProperty("username", username);
        request.addProperty("pass", password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectReciveOrders(String username, String password, int statusId) throws IOException, XmlPullParserException {

        System.out.println("ConnectUpdateInfo, " + username + " , " + password);

        String SOAP_ACTION = "http://tempuri.org/ReciveOrders";
        String METHOD_NAME = "ReciveOrders";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", username);
        request.addProperty("password", password);
        request.addProperty("statusId", statusId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectCheckedReciveOrders(int orderId, boolean IsAccepted, String Description) throws IOException, XmlPullParserException {

        System.out.println("ConnectCheckedReciveOrders, " + orderId + " , " + IsAccepted + " , " + Description);

        String SOAP_ACTION = "http://tempuri.org/CheckedReciveOrders";
        String METHOD_NAME = "CheckedReciveOrders";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", LoginHolder.getInstance().getLogin().user);
        request.addProperty("password", LoginHolder.getInstance().getLogin().pass);
        request.addProperty("orderId", orderId);
        request.addProperty("Description", Description);
        request.addProperty("IsAccepted", IsAccepted);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetCompanyInfo(int company) throws IOException, XmlPullParserException {

        System.out.println("GetCompanyInfo," + company + ",");

        String SOAP_ACTION = "http://tempuri.org/GetCompanyInfo";
        String METHOD_NAME = "GetCompanyInfo";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", company);
        request.addProperty("consumerUsername", LoginHolder.getInstance().getLoginId());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;

    }

    public SoapPrimitive ConnectGetPicsOfCompany(int company) throws IOException, XmlPullParserException {

        System.out.println("GetPicsOfCompany," + company + ",");

        String SOAP_ACTION = "http://tempuri.org/GetPicsOfCompany";
        String METHOD_NAME = "GetPicsOfCompany";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyid", company);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;

    }

    public SoapPrimitive ConnectAccess(String code) throws IOException, XmlPullParserException {
        System.out.println("ConnectUpdateInfo, " + code);

        String SOAP_ACTION = "http://tempuri.org/ShowPrice";
        String METHOD_NAME = "ShowPrice";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("companyId", Values.companyId);
        request.addProperty("username", LoginHolder.getInstance().getLogin().uid);
        request.addProperty("code", code);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetCodeForShowPrice(String code) throws IOException, XmlPullParserException {
        System.out.println("ConnectUpdateInfo, " + code);

        String SOAP_ACTION = "http://tempuri.org/GetCodeForShowPrice";
        String METHOD_NAME = "GetCodeForShowPrice";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("companyId", Values.companyId);
        request.addProperty("username", code);
        request.addProperty("pass", "Onlinep@khsh!1394.");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }


    public SoapPrimitive ConnectGetNotifications() throws IOException, XmlPullParserException {
        System.out.println("ConnectGetNotifications---->>>>>>> " + LoginHolder.getInstance().getLogin().hasAccess());

        String SOAP_ACTION = "http://tempuri.org/GetNotifications";
        String METHOD_NAME = "GetNotifications";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("companyId", Values.companyId);
        request.addProperty("IsManufacture", LoginHolder.getInstance().getLogin().hasRole);
        LoginModel aLoginModel = LoginHolder.getInstance().getLogin();
        if (aLoginModel != null)
            request.addProperty("ManufactureUsername", aLoginModel.user);
        else
            request.addProperty("ManufactureUsername", "");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectNotificationsShown(int nid) throws IOException, XmlPullParserException {
        System.out.println("NotificationsShown->>");

        String SOAP_ACTION = "http://tempuri.org/NotificationsShown";
        String METHOD_NAME = "NotificationsShown";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("NotificationsId", nid);
        request.addProperty("GeneralApp", false);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }


    public SoapPrimitive ConnectAddProduct(ObjProduct aObjProduct) throws IOException, XmlPullParserException {
        System.out.println("AddProduct->>" + aObjProduct.id);

        System.out.println("unitsInStock->"+aObjProduct.unitsInStock);
        System.out.println("price->"+aObjProduct.price);
        System.out.println("discount->"+aObjProduct.discount);
        System.out.println("minOrder->"+aObjProduct.minOrder);

        String SOAP_ACTION = "http://tempuri.org/AddProduct";
        String METHOD_NAME = "AddProduct";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        LoginModel aLoginHolder = LoginHolder.getInstance().getLogin();

        request.addProperty("username", aLoginHolder.user);
        request.addProperty("password", aLoginHolder.pass);
        request.addProperty("objProduct", new Gson().toJson(aObjProduct));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive CalculatePrice(BasketModel aBasketModel) throws IOException, XmlPullParserException {

        System.out.println("CalculatePrice->>" + aBasketModel.count + ",," + aBasketModel.aKala.id);

        String SOAP_ACTION = "http://tempuri.org/CalculatePrice";
        String METHOD_NAME = "CalculatePrice";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        LoginModel aLoginHolder = LoginHolder.getInstance().getLogin();

        request.addProperty("companyId", Values.companyId);
        request.addProperty("productId", aBasketModel.aKala.id);
        request.addProperty("quantity", aBasketModel.count);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetProductHomeSmall(int companyId, int page, int limit) throws IOException, XmlPullParserException {

        System.out.println("GetProductsSearch," + companyId + "," + page + "," + limit);

        String SOAP_ACTION = "http://tempuri.org/GetProductsSearch";
        String METHOD_NAME = "GetProductsSearch";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("productName", "");
        request.addProperty("categoryId", Values.takCatId);
        request.addProperty("pageindex", page);
        request.addProperty("pagesize", limit);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetAboutUs() throws IOException, XmlPullParserException {

        System.out.println("GetAboutUs(,");

        String SOAP_ACTION = "http://tempuri.org/GetAboutUs";
        String METHOD_NAME = "GetAboutUs";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;

    }

    public SoapPrimitive ConnectGetBill(String user, String pass) throws IOException, XmlPullParserException {

        System.out.println("ConnectCheckedReciveOrders, ");

        String SOAP_ACTION = "http://tempuri.org/GetBill";
        String METHOD_NAME = "GetBill";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", user);
        request.addProperty("password", pass);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetSendMail(String user, String subject, String body) throws IOException, XmlPullParserException {

        System.out.println("ConnectSendMail, ");

        String SOAP_ACTION = "http://tempuri.org/SendMail";
        String METHOD_NAME = "SendMail";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", user);
        request.addProperty("subject", subject);
        request.addProperty("body", body);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetHistoryOrders(String uid) throws IOException, XmlPullParserException {

        System.out.println("ConnectGetHistoryOrders " + uid);

        String SOAP_ACTION = "http://tempuri.org/GetHistoryOrders";
        String METHOD_NAME = "GetHistoryOrders";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", uid);
        request.addProperty("pass", "Onlinep@khsh!1394.");


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectGetStatusConsumerByCIdJson(String uid)  throws IOException, XmlPullParserException {

        System.out.println("ConnectGetStatusConsumerByCIdJson" + uid);

        String SOAP_ACTION = "http://tempuri.org/GetStatusConsumerByCIdJson";
        String METHOD_NAME = "GetStatusConsumerByCIdJson";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", uid);
        request.addProperty("companyId", Values.companyId);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapPrimitive ConnectBazyabi(String user) throws IOException, XmlPullParserException {

        System.out.println("ConnectBazyabi" + user);

        String SOAP_ACTION = "http://tempuri.org/Bazyabi";
        String METHOD_NAME = "Bazyabi";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("username", user);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapPrimitive response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapPrimitive) envelope.getResponse();


        System.out.println(response);

        return response;
    }

    public SoapObject ConnectGetProductSearchFilter(int companyId, String search, int page, int limit) throws IOException, XmlPullParserException {
        System.out.println("GetProductsSearch," + companyId + "," + page + "," + limit + "," + search);

        String SOAP_ACTION = "http://tempuri.org/GetProductsSearch";
        String METHOD_NAME = "GetProductsSearch";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://onlinepakhsh.com/A_onlinepakhshService.asmx?WSDL";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("companyId", companyId);
        request.addProperty("productName", search);
        request.addProperty("pageindex", page);
        request.addProperty("categoryId", 0);
        request.addProperty("pagesize", limit);
        request.addProperty("type", Values.type);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);

        SoapObject response = null;


        ht.call(SOAP_ACTION, envelope);

        response = (SoapObject) envelope.getResponse();


        System.out.println(response);

        return response;
    }

}