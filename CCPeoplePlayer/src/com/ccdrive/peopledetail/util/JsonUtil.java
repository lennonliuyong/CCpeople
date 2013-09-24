package com.ccdrive.peopledetail.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ccdrive.peopledetail.bean.MovieList;
import com.ccdrive.peopledetail.bean.OpusBean;
import com.ccdrive.peopledetail.bean.PageBean;
import com.ccdrive.peopledetail.bean.PeopleBean;
import com.ccdrive.peopledetail.bean.PeopleList;
import com.ccdrive.peopledetail.bean.TypeBean;
import com.ccdrive.peopledetail.http.HttpRequest;

public class JsonUtil {

	public static MovieList getMoiveList(String path) {
		HttpRequest httpRequest = HttpRequest.getInstance();
		System.out.println("下载的信息为=====" + path);
		MovieList movieList = new MovieList();
		if (path == null)
			return movieList;
		ArrayList<OpusBean> arrayMovie = null;
		OpusBean opusBean = null;
		PageBean pageBean = null;
		try {
			JSONObject jsonObject = new JSONObject(path);
			JSONObject jsonPage = jsonObject.getJSONObject("page");
			JSONArray jsonData = jsonObject.getJSONArray("data");
			pageBean = new PageBean();
			pageBean.setCurrentPage(jsonPage.getString("currentPage"));
			pageBean.setPageSize(jsonPage.getString("pageSize"));
			pageBean.setTotalPage(jsonPage.getString("totalPage"));
			pageBean.setTotalRows(jsonPage.getString("totalRows"));
			movieList.setPageBean(pageBean);
			if (jsonData.length() > 0) {
				arrayMovie = new ArrayList<OpusBean>();
				for (int i = 0; i < jsonData.length(); i++) {
					JSONObject jsonO = jsonData.getJSONObject(i);
					opusBean = new OpusBean();
					opusBean.setId(jsonO.getString("ID"));
					opusBean.setName(jsonO.getString("NAME"));
					String pic = jsonO.getString("PIC");
					if (pic.startsWith("http")||pic.startsWith("https")){
						opusBean.setPic(pic);
					}else {
					 String downloadpic = httpRequest.getURL_QUERY_DOWNLOAD_URL()+pic;
					 opusBean.setPic(downloadpic);
					}
//					if (httpRequest.getType().equals("23")||httpRequest.getType().equals("7")||httpRequest.getType().equals("11")) {
						opusBean.setCategory(jsonO.getString("CATEGORYNAME"));
//					}
					arrayMovie.add(opusBean);
				}
				movieList.setMovieList(arrayMovie);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieList;
	}

	@SuppressWarnings("unused")
	public static HashMap<String, Object> getpeople(String path) {
		HashMap<String, Object> peopleMap = null;
		PeopleBean peopleBean = null;
		if (path == null) {
			return peopleMap;
		}
		try {
			peopleMap = new HashMap<String, Object>();
			peopleBean = new PeopleBean();
			JSONObject jsonO = new JSONObject(path);
			peopleBean.setId(jsonO.getString("ID"));
			peopleBean.setChineseName(jsonO.getString("CHINESENAME"));
			peopleBean.setEnglishName(jsonO.getString("ENGLISHNAME"));
			peopleBean.setConstellation(jsonO.getString("CONSTELLATION"));
			peopleBean.setNationality(jsonO.getString("NATIONALITY"));
			peopleBean.setBirthday(jsonO.getString("BIRTHDAY"));
			peopleBean.setBirthPlace(jsonO.getString("BIRTHPLACE"));
			peopleBean.setProfession(jsonO.getString("PROFESSION"));
			peopleBean.setGraduated(jsonO.getString("GRADUATED"));
			peopleBean.setSummary(jsonO.getString("SUMMARY"));
			peopleBean.setPic(jsonO.getString("PIC"));
			System.out.println("pic地址" + jsonO.getString("PIC"));
			peopleMap.put("peopleBean", peopleBean);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return peopleMap;

	}

	public static PeopleList getPeopleList(String content) {
		PeopleList movieList = new PeopleList();
		if (content == null)
			return movieList;
		ArrayList<PeopleBean> arrayPeople = null;
		PeopleBean people = null;
		PageBean pageBean = null;
		try {
			JSONObject jsonObject = new JSONObject(content);
			JSONObject jsonPage = jsonObject.getJSONObject("page");
			
			JSONArray jsonData = jsonObject.getJSONArray("data");
			pageBean = new PageBean();
			if (!jsonPage.isNull("currentPage")) {
			pageBean.setCurrentPage(jsonPage.getString("currentPage"));
			}
			if (!jsonPage.isNull("pageSize")) {
				pageBean.setPageSize(jsonPage.getString("pageSize"));
			}
			if (!jsonPage.isNull("totalPage")) {
				pageBean.setTotalPage(jsonPage.getString("totalPage"));
			}
			if (!jsonPage.isNull("totalRows")) {
				pageBean.setTotalRows(jsonPage.getString("totalRows"));
			}
			movieList.setPageBean(pageBean);
			if (null!=jsonData&&jsonData.length() > 0) {
				arrayPeople = new ArrayList<PeopleBean>();
				for (int i = 0; i < jsonData.length(); i++) {
					JSONObject jsonO = jsonData.getJSONObject(i);
					people = new PeopleBean();
					people.setId(jsonO.getString("ID"));
					people.setChineseName(jsonO.getString("CHINESENAME"));
					people.setPic(jsonO.getString("PIC"));
					arrayPeople.add(people);
				}
				movieList.setMovieList(arrayPeople);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieList;
	}


	public static ArrayList<HashMap<String, Object>> getMenuList(String content){
		ArrayList<HashMap<String, Object>> menuList = new ArrayList<HashMap<String, Object>>();
		if(content==null){			
			return menuList;
		}
		try {
			JSONArray array = new JSONArray(content);
			for(int i=0,n=array.length();i<n;i++){
				HashMap<String, Object> menuDataList = new HashMap<String, Object>();
				 JSONObject jsonObject = array.getJSONObject(i);
			 TypeBean typeBean = new TypeBean();
			 typeBean.setName(jsonObject.getString("NAME"));
			 typeBean.setColumns(jsonObject.getString("COLUMNS"));
			 menuDataList.put("typeBean", typeBean);
			 JSONArray jsonArray=jsonObject.getJSONArray("data");
			 ArrayList<HashMap<String, String>> menuDataMap = getMenuData(jsonArray);		
			 menuDataList.put("menuData", menuDataMap);
			 menuList.add(menuDataList);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		return menuList;
	}
	private static ArrayList<HashMap<String, String>>  getMenuData(JSONArray jsonArray){
		ArrayList<HashMap<String, String>> menuBean = new ArrayList<HashMap<String, String>>();
		if(jsonArray.length()<0){			
			return menuBean;
		}		
		HashMap<String, String> firstMap = new HashMap<String, String>();
		firstMap.put("全部", "全部");
		menuBean.add(firstMap);
			for(int j=0,m=jsonArray.length();j<m;j++){
				HashMap<String, String> map = new HashMap<String, String>();
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(j);
					Iterator<String> keys = jsonObject.keys();
					while(keys.hasNext()){		
						String key = keys.next();
						map.put(key, jsonObject.getString(key));		
						menuBean.add(map);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return menuBean;		
	}
}
