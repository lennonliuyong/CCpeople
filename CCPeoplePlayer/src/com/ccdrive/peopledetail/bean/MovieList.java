package com.ccdrive.peopledetail.bean;

import java.util.ArrayList;

public class MovieList {

	private PageBean pageBean;
	private ArrayList<OpusBean> movieList;

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public ArrayList<OpusBean> getMovieList() {
		return movieList;
	}

	public void setMovieList(ArrayList<OpusBean> movieList) {
		if (null != movieList) {
			this.movieList = movieList;
		}
	}

}
