package com.su.android.battleship.ui.adapter;

import com.su.android.battleship.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * MoveShip Adapter
 * @author Toni
 *
 */
public class MoveShipsAdapter extends BaseAdapter {

	private static final int FIELDS_COUNT = 100;
	private static final int IMG_WATER = R.drawable.transparent;
	private static final int IMG_SHIP  = R.drawable.ship;
	private static final int IMG_MOVE  = R.drawable.ship_move;
	private static final int IMG_ERROR = R.drawable.ship_error;

	private Context mContext;
	private ImageView[] fields = new ImageView[FIELDS_COUNT];

	private short[] shipFiledIndexes;

	/**
	 * MoveShipsAdapter constructor
	 * 
	 * @param c				Activity context
	 * @param shipFields	Array with ships' starting positions on board
	 */
	public MoveShipsAdapter(Context c, short[] shipFields) {
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
		return fields[position];
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
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			imageView = new ImageView(mContext);
			// create device independent pixels
            int imageSize = (int) (30.0f * mContext.getResources().getDisplayMetrics().density);
			imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
			imageView.setPadding(0, 0, 0, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		if (isAShipField((short) position)) {
			imageView.setImageResource(IMG_SHIP);  
		} else {
			imageView.setImageResource(IMG_WATER); 
		}

		/**
		 * work-around @reference - getView method in GameBoardImageAdapter
		 */
		if (fields[position] == null) {
			fields[position] = imageView;
		}

		return imageView;
	}
	
	private boolean isAShipField(short boardFieldIndex) {
		for (int i = 0; i < shipFiledIndexes.length; i++) {
			if (boardFieldIndex == shipFiledIndexes[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return empty image resource id
	 * @return Id of the resource
	 */
	public Integer getSeaPicture() {
		return IMG_WATER;
	}

	/**
	 * Return ship image resource id
	 * @return Id of the resource
	 */
	public Integer getShipPicture() {
		return IMG_SHIP;
	}

	/**
	 * Return moving ship image resource id
	 * @return Id of the resource
	 */
	public Integer getSelectedShipPicture() {
		return IMG_MOVE;
	}
	
	/**
	 * Return forbidden field image resource id
	 * @return Id of the resource
	 */
	public Integer getForbiddenFieldPicture () {
		return IMG_ERROR;
	}
}
