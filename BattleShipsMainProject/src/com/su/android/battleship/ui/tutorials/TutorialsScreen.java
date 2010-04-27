package com.su.android.battleship.ui.tutorials;


import java.util.List;


import com.su.android.battleship.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TutorialsScreen extends Activity {

	private GridView mGrid;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorials_menu);
						
		mGrid = (GridView) findViewById(R.id.myGrid);
        mGrid.setAdapter(new ImageAdapter(TutorialsScreen.this)); 
        
        mGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
				Intent mIntent = null;
				switch (position) {
				case 0: //this is AimAndFire tutorial
					mIntent = new Intent(v.getContext(),AimAndFireTutorial.class);
					startActivity(mIntent);
					break;
				case 1: //this is Fixed ship game tutorial
					mIntent = new Intent(v.getContext(),FixShipGameTutorial.class);
					startActivity(mIntent);
					break;
				case 3: //this is move single ship tutorial
					mIntent = new Intent(v.getContext(), MoveSingleShipTutorial.class);
					startActivity(mIntent);
				default:
					break;
				}
			}        	
        });        
	}
	
	public class ImageAdapter extends BaseAdapter {
        private static final int TUTORIAL_MENU_BUTTONS_COUNT = 4;
        private Context mContext;
        private ImageView[] menuItems = new ImageView[TUTORIAL_MENU_BUTTONS_COUNT];

        private Integer[] menuPictures = {
        		R.drawable.aim_and_fire_tutorial,
        		R.drawable.fix_ship_game,
        		R.drawable.view_minimap,
        		R.drawable.move_single_ship
        };

		
		public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return menuPictures.length;
        }

        public Object getItem(int position) {
            return menuItems[position];
        }       
       
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(185, 65));                
                imageView.setPadding(2, 2, 2, 2);
            } else {
                imageView = (ImageView) convertView;
            }
            
            //ALWAYS use setImageResource and not setBackground - it disables click event somehow...
            imageView.setImageResource(menuPictures[position]);
            
            menuItems[position] = imageView;
            return imageView;
        }
    }
}
