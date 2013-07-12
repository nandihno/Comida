package org.nando.comida.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import org.nando.comida.pojo.ComidaPojo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by deleonf on 28/05/13.
 */
public class StableArrayAdapter extends ArrayAdapter<ComidaPojo> {

    HashMap<ComidaPojo,Integer> mIdMap = new HashMap<ComidaPojo, Integer>();

    public StableArrayAdapter(Context context,int textViewResourceId,List<ComidaPojo> objects) {
        super(context,textViewResourceId,objects);
        for(int i =0; i< objects.size(); i++) {
            mIdMap.put(objects.get(i),i);
        }
    }



    @Override
    public long getItemId(int position) {
        ComidaPojo pojo = getItem(position);
        return mIdMap.get(pojo);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
