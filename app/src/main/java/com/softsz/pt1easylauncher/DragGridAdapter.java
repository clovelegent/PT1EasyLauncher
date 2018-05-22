package com.softsz.pt1easylauncher;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class DragGridAdapter<T> extends BaseAdapter {
	private static final String TAG = "DragGridAdapter";
	private boolean isMove = false;
	private int movePosition = -1;
	private final List<T> list;
	Context mContext;

	public DragGridAdapter(List list, Context context) {
		this.list = list;
		mContext = context;
	}

	public List<T> getList() {
		return list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, "-------------------------------");
		for (T t : list) {
			Log.i(TAG, t.toString());
		}
		View view = getItemView(position, convertView, parent);
		/*if (position == movePosition && isMove) {
			view.setVisibility(View.INVISIBLE);
		}*/
		return view;
	}

	protected abstract View getItemView(int position, View convertView, ViewGroup parent);

	/**
	 * 给item交换位置
	 * 
	 * @param originalPosition
	 *            item原先位置
	 * @param nowPosition
	 *            item现在位置
	 */
	public void exchangePosition(int originalPosition, int nowPosition, boolean isMove) {
		T t = list.get(originalPosition);
		list.remove(originalPosition);
		list.add(nowPosition, t);
		
		/*StringBuilder sb = new StringBuilder();
		for (T ii : list) {
			sb.append(((ItemInfo)ii).componentName.getPackageName()+"/"+((ItemInfo)ii).componentName.getClassName()+";");
		}
		LoadAppsUtil.updateCompNmSort(mContext, sb.toString());*/
		
		movePosition = nowPosition;
		this.isMove = isMove;
		notifyDataSetChanged();
	}

}
