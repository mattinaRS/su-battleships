package com.su.android.battleship.ui.adapter;

import com.su.android.battleship.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MoveShipsAdapter extends BaseAdapter {

	private static final int FIELDS_COUNT = 100;

	private Context mContext;
	private ImageView[] fields = new ImageView[FIELDS_COUNT];

	private short[] shipFiledIndexes;

	public MoveShipsAdapter(Context c, short[] shipFields) {
		mContext = c;
		shipFiledIndexes = shipFields;
	}

	/**
	 * Very important method - this tells android UI framework how many times to
	 * call getView
	 */
	public int getCount() {
		return FIELDS_COUNT;
	}

	public Object getItem(int position) {
		return fields[position];
	}

	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			imageView = new ImageView(mContext);
			// TODO fix this with new design
			// create device independent pixels
            int imageSize = (int) (29.0f * mContext.getResources().getDisplayMetrics().density);
			imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
			imageView.setPadding(0, 0, 0, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		if (isAShipField((short) position)) {
			imageView.setImageResource(getShipPicture());  
		} else {
			imageView.setImageResource(getSeaPicture()); 
		}

		/**
		 * work-around @reference - getView method in GameBoardImageAdapter
		 */
		if (fields[position] == null) {
			fields[position] = imageView;
		}

		return imageView;
	}

	// references to our images

	private boolean isAShipField(short boardFieldIndex) {
		for (int i = 0; i < shipFiledIndexes.length; i++) {
			if (boardFieldIndex == shipFiledIndexes[i]) {
				return true;
			}
		}
		return false;
	}

	private Integer[] moveShipsPictures = { R.drawable.transparent,
			R.drawable.green, R.drawable.yellow, R.drawable.red };

	public Integer getSeaPicture() {
		return moveShipsPictures[0];
	}

	public Integer getShipPicture() {
		return moveShipsPictures[1];
	}

	public Integer getSelectedShipPicture() {
		return moveShipsPictures[2];
	}
	
	public Integer getForbiddenFieldPicture () {
		return moveShipsPictures[3];
	}
}
