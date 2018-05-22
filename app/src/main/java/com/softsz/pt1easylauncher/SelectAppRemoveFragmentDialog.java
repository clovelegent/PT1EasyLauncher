package com.softsz.pt1easylauncher;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author xph 
 * class for remove app .When there are some removeable app, click the menu button will show this fragment 
 *
 */
public class SelectAppRemoveFragmentDialog extends DialogFragment implements OnItemClickListener {

	private ListView allAppsListView;
	private List<ItemInfo> AllAllowRemoveItemApps = new ArrayList<ItemInfo>();
	
	public interface SelectedRemoveListener  
    {  
        void onSelectedRemoveComplete(ItemInfo itemInfo);
    }  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		for(ItemInfo ii : LoadAppsUtil.mAppsData){
			if(ii.isIsRemoveAble()){
				AllAllowRemoveItemApps.add(ii);
			}
		}
		
		View view = inflater.inflate(R.layout.select_app_fragment, null);
		((TextView)view.findViewById(R.id.select_title)).setText(R.string.select_remove_app_title);
		allAppsListView = (ListView) view.findViewById(R.id.all_app_list);
		allAppsListView.setAdapter(new ListViewAdapter(getActivity()));
		allAppsListView.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SelectedRemoveListener msListener = (SelectedRemoveListener) getActivity();
		msListener.onSelectedRemoveComplete(AllAllowRemoveItemApps.get(position));
		dismiss();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		this.dismiss();
	}

	public class ListViewAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return  AllAllowRemoveItemApps.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.select_app_item, null);
				holder.img = (ImageView) convertView.findViewById(R.id.lunch_app_img);
				holder.label = (TextView) convertView.findViewById(R.id.lunch_app_label);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackground(AllAllowRemoveItemApps.get(position).getIcon());
			holder.label.setText(AllAllowRemoveItemApps.get(position).getLabel());

			return convertView;
		}
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView label;
	}

}
