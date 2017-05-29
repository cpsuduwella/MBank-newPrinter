package wasn.mbank.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * class that construct grid view
 * @author eranga
 */
public class MBankGridViewAdapter extends BaseAdapter{

	//related activity
	private Activity activity;
	
	//hold grid view element texts
	private ArrayList<String> texts;
	
	//to hold grid view element image ids
	private ArrayList<Integer> images;
	
	/**
	 * @param activity - corresponding activity
	 * @param texts - texts list
	 * @param images - image id list
	 */
	public MBankGridViewAdapter(Activity activity,ArrayList<String>texts,ArrayList<Integer>images ) {
	
		this.activity=activity;
		this.images=images;
		this.texts=texts;
		
	}

	/**
	 * get no of items in grid view
	 */
	@Override
	public int getCount() {
		
		//return texts list size
		return texts.size();
		
	}

	/**
	 * get selected position of grid view
	 */
	@Override
	public String getItem(int position) {

		//return selected string
		return texts.get(position);
		
	}

	/**
	 * get selected item id of grid view
	 */
	@Override
	public long getItemId(int position) {
		
		return 0;
		
	}

	/**
	 * class that corresponds to grid view element
	 * @author eranga
	 */
	public static class ViewHolder{
		
		public ImageView imageViewTitle;
		public TextView textViewTitle;
		
	}
	
	/**
	 * get view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//view holder
		ViewHolder view;
		
		//get layout inflater
		LayoutInflater inflator = activity.getLayoutInflater();
		
		/*
		 * construct grid view
		 */
		if(convertView==null){
			
			view=new ViewHolder();
			
			convertView = inflator.inflate(R.layout.mbank_gridview_row_layout,null);
			
			view.textViewTitle= (TextView) convertView.findViewById(R.id.mbankGridViewText);
            view.imageViewTitle = (ImageView) convertView.findViewById(R.id.mbankGridViewImage);

            convertView.setTag(view);
			
		}
		
		else{
			
			view = (ViewHolder) convertView.getTag();
			
		}
		
		//set text and image
		view.textViewTitle.setText(texts.get(position));
		view.imageViewTitle.setImageResource(images.get(position));
		
		//return view
		return convertView;
		
	}
	
}
