package com.ccdrive.peopledetail.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpRequest {

	// private String WEB_ROOT = "http://apk.vocy.com/";
	private String WEB_ROOT;
	private String webRootDetail;
	private static HttpRequest request;
	private String id;
	private String mytoken;
	private String apkuuid;
	private String type;
	private int pagesize;
	private String detail_id;
	
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}


	private HttpRequest() {
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setWebRootDetail(String webRootDetail) {
		this.webRootDetail = webRootDetail;
	}

	public void setWEB_ROOT(String wEB_ROOT) {
		WEB_ROOT = wEB_ROOT;

	}

	public String getWEB_ROOT() {
		return WEB_ROOT;
	}

	public void setApkuuid(String apkuuid) {
		this.apkuuid = apkuuid;
	}

	public static HttpRequest getInstance() {
		if (request == null) {
			request = new HttpRequest();
		}
		return request;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMyToken(String mytoken) {
		this.mytoken = mytoken;
	}

	public String getMytoken() {
		return mytoken;
	}
	//���� �μ���ַ
	public String getURL_DETAIL(){
		return webRootDetail+"html/workplay/workplay_"+type+"_"+detail_id+"_1.txt";
	}
	
	
	// �����ļ�
	public String getURL_QUERY_DOWNLOAD_URL() {
		return WEB_ROOT + "index/download.action?token=" + mytoken
				+ "&inputPath=";
	}

	// �����б�
	// chinesename ����; sex �Ա� class ���� profession ְҵ
	public String getURL_PEOPLE_ALL_MOVIESHOP() {
		return "http://192.168.1.3:8080/androidChannelAction!actorBaseList.action?token="+mytoken+"&resultType=json&channelid="
				+ type + "&pageSize=12&currentPage=";
	}

	// ��̨���ؼ�������
	public String getURL_PEOPLE_SEARCH() {
		return "http://192.168.1.3:2014/html/actortype/actortype_"+type+".txt";
	}

	// ��������ҳ
	public String getURL_PEOPLE_ALL() {
		return "http://192.168.1.3:2014/html/actor/actor_" + type + "_" + id
				+ ".txt";
	}
	// �����Ʒ
	// id=args2&pageSize=args3&currtntPage=args4
	public String getURL_MOIVE_ALL() {
		return "http://192.168.1.3:2014/html/actorwork/actorwork_" + type + "_"+ id + "_" + pagesize + "_";
	}

	// ��ȡ���ص�uuid
	public String getURL_UPDATE_APK() {
		return "http://api.vocy.com/android!getFunction.action?arg0=droidpc_app_getversion&arg1=";
	}

	// �������ص�ַ
	public String getURL_DOWN_UPDATE_APK() {
		return "http://api.vocy.com/apk_file/" + apkuuid + ".apk";
	}

	// �ж��Ƿ��ղ�
	public String getURL_IFCOLLECT() {
		return "http://192.168.1.3:8080/androidChannelAction!isFavoriteActor.action?token="+mytoken+"&resultType=json&channelid="+type+"&id="+id;
	}

	// �ղ�
	public String getURL_COLLECT() {
		return "http://192.168.1.3:8080/androidChannelAction!addFavoriteActor.action?token="+mytoken+"&resultType=json&channelid="+type+"&id="+id;
	}

	// ȡ���ղ�
	public String getURL_CANCEL() {
		return "http://192.168.1.3:8080/androidChannelAction!delFavoriteActor.action?token="+mytoken+"&resultType=json&channelid="+type+"&id=";
	}

	// �ղ��б�
	public String getURL_List_COLLECT() {
		return "http://192.168.1.3:8080/androidChannelAction!favoriteActor.action?token="+mytoken+"&channelid="+type+"&resultType=json&pageSize=12&currentPage=";
	}

	public static final String webServiceURL = "http://service.vocy.com/services/";
}
