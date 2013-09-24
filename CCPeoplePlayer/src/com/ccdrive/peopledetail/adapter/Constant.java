package com.ccdrive.peopledetail.adapter;

import java.io.File;

/*
 * 一些锟侥筹拷锟斤拷
 */
public class Constant {

	public static final String SOFT_FIELD = "soft";
	public static final String BOOK_FIELD = "book";
	public static final String PAPER_FIELD = "paper";
	public static final String PRINT_FIELD = "journal";
	public static final String MOVIE_FIELD = "movie";
	public static final String TV_FIELD = "tvplay";
	public static final String ANIME_FIELD = "cartoon";
	public static final String MUSIC_FIELD = "music";
	public static final String RECRD_FIELD = "tape";

	public static File FILE_NAME = null;
	public static String filePath = "";
	public static final int FLFG = 0x1;
	public static final int AL = 0x2;
	public static final int FLWSYS = 0x3;
	public static final int MOVIE = 0x4;
	public static final int RECRD = 0x5;
	public static final int TV = 0x6;
	public static final int ANIME = 0x7;
	public static final int MUSIC = 0x8;
	public static final int SOFT = 0x9;

	public static final int ALL_MOVIES = 10004;
	public static final int ALL_RECORD = 10005;// 锟斤拷锟斤拷锟叫憋拷
	public static final int SINGLE_RECORD = 100051;// 锟斤拷锟斤拷锟斤拷锟斤拷
	public static final int SINGLE_IMAGE = 100052;// 图片
	public static final int DOWNLOAD_LIST = 100053;//
	public static final int DOWNLOAD_CHECK = 10006;// 证
	public static final int ORDER_SOFT_LIST = 100091;//
	public static final int ORDER_SOFT_SINGLE = 100092;//
	public static final int ORDER_LIST = 100093;//
	public static final int ORDER_LIST_NUM = 100094;//
	public static final int ORDER_LIST_ID = 100095;// 锟斤拷锟斤拷ID
	public static final int ORDER_LIST_PAY = 100000;// 支锟斤拷
	public static final int DOWN_SOFT_LIST = 100001;//
	public static final int USERLOGIN = 100010;//

	// 菜单类型
	 public final static int KEYCODE_SERCH=0;
//	public final static int KEYCODE_SERCH = 84;

	public final static int STYLE_MOVIE = 1;
	public final static int STYLE_PEOPLE = 2;
	public final static int STYLE_CARD = 3;
	public final static int STYLE_COLLECT = 4;
	public final static int STYLE_APP = 5;

	// 消息类型
	public static final int MSG_LOAD_MOVIEINFO = 0;
	public static final int MSG_LOAD_MOVIE = 1;
	public static final int MSG_SHOW_FLITER = 2;
	public static final int MSG_LOAD_ERR = 4;

	// 商店或收藏
	public static final int STORE = 0;
	public static final int COLLECT = 1;

	// 添加为收藏或取消收藏
	public static final int CARD_UNCOLLECT = 0;
	public static final int CARD_COLLECT = 1;
	// 安装或卸载
	public static final int APP_UNINSTALL = 0;
	public static final int APP_INSTALL = 1;

	// Activity返回Code值
	// 取消收藏
	public static final int RESULT_CODE_CANCLE_COLLECT = 100;
	// 安装或卸载
	public static final int RESULT_CODE_INSTALL_STATE = 101;
	
	//沒有相關數據
	public static final int MSG_LOAD_NODATA = 102;
	//是否静默安装
	public static final boolean isSlientInstall = false;
	

}
