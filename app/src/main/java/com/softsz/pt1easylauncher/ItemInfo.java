package com.softsz.pt1easylauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Represents an item in EasyLauncher.
 */
public class ItemInfo {
	private static final String TAG = "ItemInfo";
	
	/**
	 * mark the app is removeable
	 */
	boolean IsRemoveAble = false;
	
	 /**
     * A bitmap version of the application icon.
     */
    Drawable icon;
	
    String label;
    
    /**
     * represent the app's ComponentName for item
     */
    ComponentName componentName;

	public ItemInfo(boolean isRemoveAble, Drawable icon, String label, ComponentName componentName) {
		super();
		IsRemoveAble = isRemoveAble;
		this.icon = icon;
		this.label = label;
		this.componentName = componentName;
	}

	public boolean isIsRemoveAble() {
		return IsRemoveAble;
	}

	public void setIsRemoveAble(boolean isRemoveAble) {
		IsRemoveAble = isRemoveAble;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ComponentName getComponentName() {
		return componentName;
	}

	public void setComponentName(ComponentName componentName) {
		this.componentName = componentName;
	}
	
	private String componentNameString(){
		if(componentName!=null){
			return componentName.toString();
		}else{
			return null;
		}
	}

	@Override
	public String toString() {
		return "ItemInfo [IsRemoveAble=" + IsRemoveAble + ", iconBitmap=" + icon + ", label=" + label
				+ ", componentName=" + componentNameString() + "]";
	}
    
    
}
