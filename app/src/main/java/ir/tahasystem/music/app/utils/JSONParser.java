package ir.tahasystem.music.app.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {
    }

    public String getJSONFromUrl(String urls) {

        // Making HTTP request
        try {
            // defaultHttpClient
            //DefaultHttpClient httpClient = new DefaultHttpClient();
            // HttpPost httpPost = new HttpPost(url);

            // HttpResponse httpResponse = httpClient.execute(httpPost);
            // HttpEntity httpEntity = httpResponse.getEntity();

            URL url = new URL(urls);

            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

             is = urlConnection.getInputStream();

            // is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            json = sb.toString();
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;

    }
}