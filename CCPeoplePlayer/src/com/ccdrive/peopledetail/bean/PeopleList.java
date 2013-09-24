package com.ccdrive.peopledetail.bean;

import java.util.ArrayList;

public class PeopleList {

	private PageBean pageBean;
	private ArrayList<PeopleBean> peopleList;

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public ArrayList<PeopleBean> getPeopleList() {
		return peopleList;
	}

	public void setMovieList(ArrayList<PeopleBean> peopleList) {
		this.peopleList = peopleList;
	}

}
