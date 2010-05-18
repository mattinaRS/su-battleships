package com.su.android.battleship.ui.adapter;

import com.su.android.battleship.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
/**
 * MinimapImageAdapter
 * @author vasko
 *
 */
public class MinimapImageAdapter extends BaseAdapter {

	private static final int FIELDS_COUNT = 100;
	private static final int IMG_WATER = R.drawable.transparent;
	private static final int IMG_CRASH = R.drawable.crash_mini;
	private static final int IMG_MISS  = R.drawable.miss_mini;
	private static final int IMG_SHIP  = R.drawable.ship_mini;
	
	private Context mContext;	
	private ImageView[] minimapFields = new ImageView[FIELDS_COUNT];
	
	private short[] shipFiledIndexes;
	
	/**
	 * MinimapImageAdapter constructor
	 * 
	 * @param c				Activity context
	 * @param shipFields	Array with ships' starting positions in minimap board
	 */
    public MinimapImageAdapter(Context c, short[] shipFields) {
        mContext = c;
        shipFiledIndexes = shipFields;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return FIELDS_COUNT;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return minimapFields[position];
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            // create device independent pixels
            int imageSize = (int) (10.0f * mContext.getResources().getDisplayMetrics().density);
            imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));            
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        if(isAShipField((short) position)){
        	imageView.setImageResource(IMG_SHIP); //green represents ship on the minimap
        }else{
        	imageView.setImageResource(IMG_WATER); //blue represents water on the minimap
        }
        
        /**
         * work-around @reference - getView method in GameBoardImageAdapter
         */
        if(minimapFields[position] == null){
        	minimapFields[position] = imageView;
        }
        
        return imageView;
    }
    
    /*
     * (non-Javadoc)
     * @see android.widget.BaseAdapter#areAllItemsEnabled()
     */
	public boolean areAllItemsEnabled() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	public boolean isEnabled(int position) {
		return false;
	}

	private boolean isAShipField(short boardFieldIndex){
    	for(int i = 0 ; i < shipFiledIndexes.length ; i++){
    		if(boardFieldIndex == shipFiledIndexes[i]){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Set ImageView at a given position to Crash image
     * 
     * @param position	Integer value representing position index in minimap board
     */
    public void setCrash(short position) {
    	((ImageView) getItem(position)).setImageResource(IMG_CRASH);
    }
    
    /**
     * Set ImageView at a given position to Miss image
     * 
     * @param position	Integer value representing position index in minimap board
     */
    public void setMiss(short position) {
    	((ImageView) getItem(position)).setImageResource(IMG_MISS);
    }

}
