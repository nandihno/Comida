package org.nando.comida.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by fernandoMac on 29/05/13.
 */
public class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {

    ImageView imageView;

    public DownloadImageTask(ImageView aImageView) {
        imageView = aImageView;
    }


    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap  = null;
        try {
            InputStream in = new URL(urls[0]).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch(Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
