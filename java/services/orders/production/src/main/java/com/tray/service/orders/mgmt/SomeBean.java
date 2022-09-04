package com.tray.service.orders.mgmt;

import com.tray.service.orders.db.Education;

public class SomeBean implements SomeBeanWebpiecesManaged {

	private int count;
	private Class<?> clazz = SomeBean.class;
	private Education educationLevel;
	
	@Override
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public String getCategory() {
		return "General";
	}

	@Override
	public Class<?> getPropertyNoConverter() {
		return clazz;
	}

	@Override
	public void setPropertyNoConverter(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Education getEducationLevel() {
		return educationLevel;
	}

	@Override
	public void setEducationLevel(Education educationLevel) {
		this.educationLevel = educationLevel;
	}

}
