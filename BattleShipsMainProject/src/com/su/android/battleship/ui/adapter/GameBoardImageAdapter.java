package com.su.android.battleship.ui.adapter;

import com.su.android.battleship.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GameBoardImageAdapter extends BaseAdapter {
	
	private static final int FIELDS_COUNT = 100;
	
	private Context mContext;	
	private ImageView[] boardFields = new ImageView[FIELDS_COUNT];

    public GameBoardImageAdapter(Context c) {
        mContext = c;        
    }

    /**
     * Very important method - this tells android UI framework how many times to call getView
     */
    public int getCount() {
    	Log.d("ImageViewAdapter","geCount(): "+FIELDS_COUNT);
        return FIELDS_COUNT;
    }
    
    public int getViewTypeCount(){
    	return FIELDS_COUNT;
    }

    public Object getItem(int position) {
        return boardFields[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(29, 29));            
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        
        imageView.setImageResource(boardPictures[0]);
        Log.d("ImageViewAdapter","Position is: "+position + " and convertView: "+convertView+" and parent: "+parent);
        
        
        /**
         * Due to the splendid GridView implementation of the Google team , there are no guarantees how many times
         * this method will be called for every view.In order to achieve some sort of optimizations , this method
         * is called multiple times for the first gridView element (but only in this particular case). For some reason, the 
         * second call WILL , but the third MAY NOT use the convertView and thus , a new ImageView will be created.So the first
         * itemView reference will be lost.The displayed ItemView for every position in the GridView however is the returned itemView
         * form the FIRST call to this method.And if we store the ItemView references each time this method is called, the old
         * ones will be lost if this method is called THREE times for the same position, which in the end causes the bug.
         * So in conclusion either the Google team had something messed up with the implementation , or , more likely , the 
         * ImageAdapter is not the best way to handle this kind of situation.One guess is that one can never know for sure , before
         * one learns and understands Google's Activity/View Design Model in deep details - which can obviously be assumed to be
         * impossible task if one is SIMPLY a mortal one. 
         * 
         * @reference : http://www.mailinglistarchive.com/android-developers@googlegroups.com/msg17982.html
         */
        if(boardFields[position] == null){
        	boardFields[position] = imageView;
        }        
        return imageView;
    }

    // references to our images
    private Integer[] boardPictures = {
    		R.drawable.transparent, R.drawable.crash , R.drawable.miss , R.drawable.yellow
    };
    
    /**
     * Get the Integer id for crash image
     * @return
     */
    public Integer getTransparent(){
    	return boardPictures[0];
    }
    
    /**
     * Get the Integer id for crash image
     * @return
     */
    public Integer getCrash(){
    	return boardPictures[1];
    }
    
    /**
     * Get the Integer id for miss image
     * @return
     */
    public Integer getMiss(){
    	return boardPictures[2];
    }
    
    /**
     * Get the Integer id for miss image
     * @return
     */
    public Integer getCrosair(){
    	return boardPictures[3];
    }
    
}
