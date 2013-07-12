package org.nando.comida;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.nando.comida.adapters.StableArrayAdapter;
import org.nando.comida.pojo.ComidaPojo;
import org.nando.comida.tasks.DownloadImageTask;
import org.nando.comida.tasks.RestFoodForkTask;
import org.nando.comida.tasks.RestTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Button searchBtn;
    private EditText searchEdit;
    private TextView infoText;
    private ListView listView;
    private ImageView imageView;
    private Button clearBtn;

    private ComidaPojo pojo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchEdit = (EditText) findViewById(R.id.comidaTxtField);
        infoText = (TextView) findViewById(R.id.infoMessage);
        listView = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.imageView);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pojo = (ComidaPojo) adapterView.getItemAtPosition(i);
                if(pojo.image != null) {
                    new DownloadImageTask(imageView).execute(pojo.image);
                }

               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pojo.link));
                startActivity(intent);
                */

            }
        });

    }

    public void evernoteShare(MenuItem item) {

        ArrayList <String> tags = new ArrayList();
        tags.add(pojo.title);
        tags.add("Recipes");
        tags.add("recetas");

        Intent sendIntent = new Intent();
        sendIntent.setAction("com.evernote.action.CREATE_NEW_NOTE");
        sendIntent.putExtra(Intent.EXTRA_TITLE, "Recipes");
        sendIntent.putExtra(Intent.EXTRA_TEXT,pojo.title + "\n"+pojo.link);
        sendIntent.putExtra("TAG_NAME_LIST",tags);
        sendIntent.putExtra("SOURCE_URL",pojo.link);
        sendIntent.putExtra("QUICK_SEND",true);
       // sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void chooseToShare(MenuItem item) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Recipe "+pojo.title);
        sharingIntent.putExtra(Intent.EXTRA_TITLE,"Recipes");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,pojo.title+ "\n"+pojo.link);
        startActivity(Intent.createChooser(sharingIntent,"Share via"));
    }

    public void takeMeToSite(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(pojo.link));
        startActivity(intent);

    }

    public void pressToClearButton(View view) {
        infoText.setText("");
        searchEdit.setText("");
        pojo = new ComidaPojo();
        listView.setAdapter(null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void pressButton2(View view) {
        String txt = searchEdit.getText().toString();
        RestFoodForkTask task = null;
        if(txt.isEmpty()) {
            task = new RestFoodForkTask(this);
        } else {
            task = new RestFoodForkTask(this,txt);
        }
        this.infoText.setText("");
        task.execute("http://food2fork.com/api/search");
    }



    public void pressButton(View view) {
        String txt = searchEdit.getText().toString();
        RestTask task = null;
        if(txt.isEmpty()) {
            task = new RestTask(this);
        } else {
            task = new RestTask(this,txt);
        }
        this.infoText.setText("");
        task.execute("http://apiservice.univision.com/rest/feed/getFeed?url=");
        //task.execute("http://food2fork.com/api/search");
    }

    public void placeResults(List<ComidaPojo> list) {
        boolean foundIssue = false;
        for(ComidaPojo pojo:list) {
            if(pojo.message != null) {
                infoText.setText(pojo.message);
                foundIssue = true;
                break;
            }
        }
        if(foundIssue == false) {
            StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1,list);
            listView.setAdapter(adapter);
        }

    }
    
}
