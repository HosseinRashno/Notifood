package com.notifood.notifoodlibrary.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.notifood.notifoodlibrary.R;
import com.notifood.notifoodlibrary.models.ServiceModel.RequestModel;
import com.notifood.notifoodlibrary.models.ServiceModel.ResponseModel;
import com.notifood.notifoodlibrary.utils.Declaration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by mrashno on 10/5/2017.
 */

public class SettingsAndRestaurantsService extends AsyncTask<String, Integer, ResponseModel> {

    private String urlString;
    private RequestModel requestModel;
    private ServiceDelegate delegate;
    private Context context;

    public interface ServiceDelegate{
        public void serviceCompletionResult(ResponseModel response);
    }

    public SettingsAndRestaurantsService(RequestModel requestModel, ServiceDelegate delegate, Context context){
        super();
        this.requestModel = requestModel;
        this.delegate = delegate;
        this.context = context;
        urlString = context.getString(R.string.url_setting_and_restaurant);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponseModel doInBackground(String... params) {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        ResponseModel responseModel = null;
        URL url = null;
        String requestJson = "";
        String responseJson = "";

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        requestJson = gson.toJson(requestModel);

        try{
            url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(context.getResources().getInteger(R.integer.int_read_timeout));
            urlConnection.setConnectTimeout(context.getResources().getInteger(R.integer.int_connect_timeout));
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(requestJson);
            writer.flush();
            writer.close();
            os.close();

            int responseCode=urlConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                try {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        responseJson += line;
                    }
                    responseModel = gson.fromJson(responseJson, ResponseModel.class);

                } finally {
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException muex){
            Log.e(Declaration.TAG, muex.getMessage());
        } catch (Exception ex){
            Log.e(Declaration.TAG, ex.getMessage());
        }
        return responseModel;
    }

    @Override
    protected void onPostExecute(ResponseModel responseModel) {
        super.onPostExecute(responseModel);
        delegate.serviceCompletionResult(responseModel);
    }
}
