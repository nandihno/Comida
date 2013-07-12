package org.nando.comida.tasks;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nando.comida.MainActivity;
import org.nando.comida.pojo.ComidaPojo;
import org.nando.comida.utils.HelperUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deleonf on 29/05/13.
 */
public class RestFoodForkTask extends AsyncTask<String,Void,List<ComidaPojo>> {

    private MainActivity myActivity;
    private String recipeVal;

    public RestFoodForkTask(MainActivity activity,String aRecipeVal) {
        myActivity = activity;
        recipeVal = aRecipeVal;
    }

    public RestFoodForkTask(MainActivity activity) {
        myActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        //System.setProperty("http.proxyHost", "proxy.qtc.com.au");
        //System.setProperty("http.proxyPort", "8080");
    }

    @Override
    protected List<ComidaPojo> doInBackground(String... strings) {
        List<ComidaPojo> list = new ArrayList();
        try {
            HttpClient client = new DefaultHttpClient();
            String qryString = "";
            String key = "key=c9679beb71cbabff72682d261ce558e6";
            if(recipeVal == null) {
                qryString = HelperUtils.qryStringBuilder(key);
            }
            else {
                qryString = HelperUtils.qryStringBuilder(key,"q="+recipeVal);
            }
            HttpGet httpGet = new HttpGet(strings[0]+qryString);
            HttpResponse response = client.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonResponse);
                JSONArray jArray = json.getJSONArray("recipes");
                for(int i =0; i < jArray.length(); i++) {
                    ComidaPojo pojo = new ComidaPojo();
                    JSONObject obj = jArray.getJSONObject(i);
                    pojo.title = obj.getString("title");
                    pojo.link = obj.getString("source_url");
                    if(obj.has("image_url")) {
                        pojo.image = obj.getString("image_url");
                    }
                    list.add(pojo);
                }
            }
            else {
                ComidaPojo pojo = new ComidaPojo();
                pojo.message = response.getStatusLine().getReasonPhrase()+" "+response.getStatusLine().getStatusCode();
                list.add(pojo);
            }
        } catch(Exception e) {
            ComidaPojo pojo = new ComidaPojo();
            pojo.message = e.getMessage();
            list.add(pojo);
        }
        return list;
    }

    protected void onPostExecute(List<ComidaPojo> list) {
        this.myActivity.placeResults(list);
    }
}
