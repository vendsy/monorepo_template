package com.tray.service.orders.mgmt;

import com.tray.service.orders.db.Education;

public interface SomeBeanWebpiecesManaged {

	public String getCategory();
	
	public void setCount(int count);
	
	public int getCount();
	
	//This demonstrates we read since no converter was found
	public Class<?> getPropertyNoConverter();
	public void setPropertyNoConverter(Class<?> clazz);
	
	//This demonstrates use of a converter
	public Education getEducationLevel();
	public void setEducationLevel(Education educationLevel);
	
}
