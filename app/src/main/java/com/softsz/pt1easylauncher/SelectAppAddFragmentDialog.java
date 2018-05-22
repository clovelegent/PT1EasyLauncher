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
 * class for add app .When there are one or more icon place, click the add icon will show this fragment 
 *
 */
public class SelectAppAddFragmentDialog extends DialogFragment implements OnItemClickListener {

	private ListView allAppsListView;
	private List<ItemInfo> AllAllowAddItemApps = new ArrayList<ItemInfo>();
	
	public interface SelectedAddListener  
    {  
        void onSelectedAddComplete(ItemInfo itemInfo);
    }  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.select_app_fragment, null);
		
		QsLunchApps mQsLunchApps = new QsLunchApps(getActivity());
		List<LunchAppInfo> mLunchAppInfos = mQsLunchApps.getActivityList(null, android.os.Process.myUserHandle());
		for (LunchAppInfo lai : mLunchAppInfos) {
			boolean stored = false;
			for(ItemInfo mad : LoadAppsUtil.mAppsData){
				if(lai.getComponentName().equals(mad.getComponentName())){
					stored = true;
					break;
				}
			}
			if(!stored){
				AllAllowAddItemApps.add(new ItemInfo(true, lai.getIcon(78), lai.getLabel().toString(), lai.getComponentName()));
			}
		}
		
		allAppsListView = (ListView) view.findViewById(R.id.all_app_list);
		allAppsListView.setAdapter(new ListViewAdapter(getActivity()));
		allAppsListView.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SelectedAddListener msListener = (SelectedAddListener) getActivity();
		msListener.onSelectedAddComplete(AllAllowAddItemApps.get(position));
		dismiss();
	}

	public class ListViewAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return  AllAllowAddItemApps.size();
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

			holder.img.setBackground(AllAllowAddItemApps.get(position).getIcon());
			holder.label.setText(AllAllowAddItemApps.get(position).getLabel());

			return convertView;
		}
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView label;
	}

}
