package com.su.android.battleship.ui.adapter;

import com.su.android.battleship.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MinimapImageAdapter extends BaseAdapter {

private static final int FIELDS_COUNT = 100;
	
	private Context mContext;	
	private ImageView[] minimapFields = new ImageView[FIELDS_COUNT];
	
	private short[] shipFiledIndexes;
	
    public MinimapImageAdapter(Context c,short[] shipFields) {
        mContext = c;
        shipFiledIndexes = shipFields;
    }

    /**
     * Very important method - this tells android UI framework how many times to call getView
     */
    public int getCount() {
        return FIELDS_COUNT;
    }

    public Object getItem(int position) {
        return minimapFields[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(10, 10));            
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        if(isAShipField((short) position)){
        	imageView.setImageResource(minimapPictures[2]); //green represents ship on the minimap
        }else{
        	imageView.setImageResource(minimapPictures[0]); //blue represents water on the minimap
        }
        
        /**
         * work-around @reference - getView method in GameBoardImageAdapter
         */
        if(minimapFields[position] == null){
        	minimapFields[position] = imageView;
        }
        
        return imageView;
    }

    // references to our images
    private Integer[] minimapPictures = {
    		R.drawable.transparent, R.drawable.crash_mini , R.drawable.ship_mini
    };
    
    private boolean isAShipField(short boardFieldIndex){
    	for(int i = 0 ; i < shipFiledIndexes.length ; i++){
    		if(boardFieldIndex == shipFiledIndexes[i]){
    			return true;
    		}
    	}
    	return false;
    }
    
    public int getCrash(){
    	return minimapPictures[1];
    }

}
