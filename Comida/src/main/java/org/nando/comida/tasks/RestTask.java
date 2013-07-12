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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deleonf on 28/05/13.
 */
public class RestTask extends AsyncTask<String,Void,List<ComidaPojo>> {

    private MainActivity myActivity;
    private String searchVal;

    public RestTask(MainActivity activity, String aSearchVal) {
        this.myActivity = activity;
        this.searchVal = aSearchVal;
    }

    public RestTask(MainActivity activity) {
        this.myActivity = activity;
    }

    protected void onPreExecute() {
        System.setProperty("http.proxyHost", "proxy.qtc.com.au");
        System.setProperty("http.proxyPort", "8080");
    }

    @Override
    protected List<ComidaPojo> doInBackground(String... strings) {
        List<ComidaPojo> list = new ArrayList();
        try {
            HttpClient client = new DefaultHttpClient();
            String qryString = "/search/recipes?count=10";
            if(searchVal != null) {
                qryString = qryString + "&text="+searchVal;
            }
            qryString = qryString +"&outputMode=json";
            String encodedQryString = URLEncoder.encode(qryString,"UTF-8");
            String url = strings[0]+encodedQryString+"&api_key=q2pq6mgejgrbbewnzx7zx2ye";
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonResponse);
                JSONArray jTitleArr = json.getJSONObject("uimJson").getJSONArray("item");
                //JSONArray jLinkArr = json.getJSONArray("link");
                for(int i=0; i < jTitleArr.length(); i++) {
                    ComidaPojo pojo = new ComidaPojo();
                    JSONObject obj = jTitleArr.getJSONObject(i);
                    pojo.title = obj.getString("title");
                    pojo.link = obj.getString("uol_mobileUrl");
                    if(obj.has("uol_largeThumbnail")) {
                        pojo.image = obj.getString("uol_largeThumbnail");
                    }
                    list.add(pojo);
                }
            }
            else {
                ComidaPojo pojo = new ComidaPojo();
                pojo.message = response.getStatusLine().getReasonPhrase()+" "+response.getStatusLine().getStatusCode();

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
