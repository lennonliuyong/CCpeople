package com.ccdrive.peopledetail.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.peopledetail.adapter.Constant;
import com.ccdrive.peopledetail.adapter.EduGridAdapter;
import com.ccdrive.peopledetail.adapter.GridAdapter;
import com.ccdrive.peopledetail.adapter.SportsGridAdapter;
import com.ccdrive.peopledetail.app.App;
import com.ccdrive.peopledetail.bean.MovieList;
import com.ccdrive.peopledetail.bean.OpusBean;
import com.ccdrive.peopledetail.bean.PeopleBean;
import com.ccdrive.peopledetail.http.HttpRequest;
import com.ccdrive.peopledetail.util.AppUtil;
import com.ccdrive.peopledetail.util.HttpUtil;
import com.ccdrive.peopledetail.util.ImageAsyncLoader;
import com.ccdrive.peopledetail.util.JsonUtil;
import com.ccdrive.peopledetail.util.LoginUtil;
import com.ccdrive.peopledetail.util.SoapUtils;
import com.ccdrive.peopledetail.util.ToastUtil;
import com.ccdrive.peopledetail.util.UpdateVersion;
import com.ccdrive.peopledetail.view.CCmyTextView;
import com.ccdrive.peopledetail.view.MyTextView;

public class MainActivity extends Activity implements OnClickListener {
	private static final int MSG_LOAD_MOVIEINFO = 0;
	private static final int MSG_LOAD_MOVIE = 1;
	private static final int MSG_LOAD_ERR = 4;
	private Context context;
	private GridView gridView, edu_gridview, other_gridview;
	private GridAdapter gridAdapter;
	private Button btn_pageUp, btn_collect;
	private Button btn_pageDown;
	private TextView tv_currentpage;
	private TextView tv_totalrows;
	private TextView tv_type;
	private Map<String, String> paramMap;
	private MovieList moiveList;
	private HashMap<String, Object> peopleMap;
	private HttpRequest uRLPath;
	private Handler handler;
	private int currentPage = 1;
	private int totalPage;
	private int totalRows;
	private boolean pageFlag = true;
	private boolean isTiming = true;
	private View preView;
	private List<OpusBean> list;
	private Runnable changDatas = new Runnable() {

		@Override
		public void run() {
			MainActivity.this.handler.sendEmptyMessage(MSG_LOAD_MOVIE);
		}
	};
	private TextView tv_chinaname, tv_foreigname, tv_nationality,
			tv_constellation, tv_id_intro;
	private LinearLayout ll_id_chinaname, ll_id_foreigname, ll_id_nationality,
			ll_id_constellation;
	private MyTextView tv_intro;
	private ImageView iv_logo;
	private ImageAsyncLoader imageLoader;
	private AQuery aQuery;
	private RelativeLayout layoutview;
	private LinearLayout line_movie_detail;
	private RelativeLayout re_movie_loading;
	private final static int beginConnectData = 500000;
	private String testPath;

	private String dataPath;
	private String myMove;
	private String content;
	private String id;
	private EduGridAdapter egridAdapter;
	private GridView allgridview;
	private SportsGridAdapter otherAdapter;
	protected ProgressBar pb_bar;
	protected TextView tv_progressbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		initObject();
	}

	private void initView() {

		btn_pageUp = (Button) findViewById(R.id.btn_pageup);
		btn_pageDown = (Button) findViewById(R.id.btn_pagedown);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_totalrows = (TextView) findViewById(R.id.tv_totalrows);
		tv_currentpage = (TextView) findViewById(R.id.tv_currentpage);

		gridView = (GridView) findViewById(R.id.grid_listt); // 电影、电视剧人物
		edu_gridview = (GridView) findViewById(R.id.edu_grid_list);// 音乐人物
		other_gridview = (GridView) findViewById(R.id.sports_grid_list); // 其它

		iv_logo = (ImageView) findViewById(R.id.iv_logo); // logo
		tv_chinaname = (TextView) findViewById(R.id.tv_chinaname); // 中文名
		ll_id_chinaname = (LinearLayout) findViewById(R.id.ll_id_chinaname);

		tv_foreigname = (TextView) findViewById(R.id.tv_foreigname); // 英文名
		ll_id_foreigname = (LinearLayout) findViewById(R.id.ll_id_foreigname);

		tv_nationality = (TextView) findViewById(R.id.tv_nationality); // 国籍
		ll_id_nationality = (LinearLayout) findViewById(R.id.ll_id_nationality);

		tv_constellation = (TextView) findViewById(R.id.tv_constellation); // 星座
		ll_id_constellation = (LinearLayout) findViewById(R.id.ll_id_constellation);

		tv_id_intro = (TextView) findViewById(R.id.tv_id_intro);
		// tv_intro = (MyTextView) findViewById(R.id.tv_intro); // 人物简介
		btn_pageDown.setOnClickListener(this);
		btn_pageUp.setOnClickListener(this);
		String type = uRLPath.getType();

		if (type.equals("0")) {
			edu_gridview.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			other_gridview.setVisibility(View.GONE);

//			edu_gridview.requestFocus();
//			edu_gridview.setFocusable(true);
			edu_gridview.setSelector(new ColorDrawable(0));
			edu_gridview.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			egridAdapter = new EduGridAdapter(this, null);
			edu_gridview.setAdapter(egridAdapter);
			edu_gridview.setFocusableInTouchMode(true);
			edu_gridview.setNextFocusUpId(R.id.btn_collect);
			uRLPath.setPagesize(12);

		} else if (type.equals("1") || type.equals("22")||type.equals("23")) {
			gridView.setVisibility(View.VISIBLE);
			edu_gridview.setVisibility(View.GONE);
			other_gridview.setVisibility(View.GONE);
			gridView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			gridView.setSelector(new ColorDrawable(0));
			gridAdapter = new GridAdapter(this, null, gridView);
			gridView.setAdapter(this.gridAdapter);
			gridView.setFocusableInTouchMode(true);
			uRLPath.setPagesize(12);

		} else {
			other_gridview.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			edu_gridview.setVisibility(View.GONE);
			other_gridview.requestFocus();
			other_gridview.setFocusable(true);
			other_gridview.setSelector(new ColorDrawable(0));
			other_gridview.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			otherAdapter = new SportsGridAdapter(MainActivity.this);
			other_gridview.setAdapter(otherAdapter);
			other_gridview.setFocusableInTouchMode(true);
			other_gridview.setNextFocusUpId(R.id.btn_collect);
			uRLPath.setPagesize(8);
		}

	}

	private void initObject() {
		aQuery = new AQuery(MainActivity.this);
		uRLPath = HttpRequest.getInstance();
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		System.out.println("人物播放器ID为" + id);
		String people = intent.getStringExtra("people");
		uRLPath.setId(id);
		content = uRLPath.getURL_MOIVE_ALL() + currentPage + ".txt";

		System.out.println("contentpci地址" + content);
		if ("1".equals(people)) {
			myMove = HttpRequest.getInstance().getURL_PEOPLE_ALL();
		}
		System.out.println("下载的地址为" + myMove);

		String path = HttpRequest.getInstance().getURL_IFCOLLECT();
		System.out.println("判断----->收藏返回为------>" + path);
		btn_collect = (Button) findViewById(R.id.btn_collect); // 收藏
		btn_collect.setOnClickListener(this);

		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					System.out.println("进入时收藏状态是-------->>>>" + object);
					if (object.contains("false")) {
						btn_collect.setTag("收藏");
						btn_collect.setEnabled(false);
						btn_collect.setFocusable(false);
						btn_collect.setVisibility(View.VISIBLE);
						btn_collect
								.setBackgroundResource(R.drawable.collect_selector);
					} else {
						btn_collect.setTag("已收藏");
						btn_collect.setEnabled(false);
						btn_collect.setFocusable(false);
						btn_collect.setVisibility(View.VISIBLE);
						btn_collect
								.setBackgroundResource(R.drawable.cancel_selector);
						System.out.println("收藏为true");
					}
				}
				super.callback(url, object, status);
			}
		});
		list = new ArrayList<OpusBean>();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {

				case MSG_LOAD_MOVIEINFO:
					getOpus();
					break;
				case MSG_LOAD_MOVIE:
					initData();
					break;
				case MSG_LOAD_ERR:
					Toast.makeText(MainActivity.this, R.string.load_error, 0)
							.show();
					break;

				case beginConnectData:
					initView();
					initListener();
					/*
					 * Intent intent = getIntent(); id =
					 * intent.getStringExtra("id");
					 * System.out.println("人物播放器ID为" + id); String people =
					 * intent.getStringExtra("people"); uRLPath.setId(id);
					 * content = uRLPath.getURL_MOIVE_ALL() + currentPage +
					 * ".txt";
					 * 
					 * System.out.println("contentpci地址" + content); if
					 * ("1".equals(people)) { myMove =
					 * HttpRequest.getInstance().getURL_PEOPLE_ALL(); }
					 * System.out.println("下载的地址为" + myMove);
					 */
					getInfo();
					break;
				default:
					break;

				}
			}

		};
		startBefor();
	}

	private void initData() {
		new Thread() {

			@Override
			public void run() {
				content = HttpUtil.getResponseString(uRLPath
						.getURL_PEOPLE_ALL());

				myMove = HttpUtil.getResponseString(uRLPath.getURL_MOIVE_ALL()
						+ currentPage + ".txt");
				if (null != content && !"".equals(content)) {
					moiveList = JsonUtil.getMoiveList(myMove);
					ArrayList<OpusBean> moviesList = moiveList.getMovieList();
					peopleMap = JsonUtil.getpeople(content);
					if (moiveList != null && peopleMap != null) {
						MainActivity.this.handler
								.sendEmptyMessage(MSG_LOAD_MOVIEINFO);
						return;
					}
					MainActivity.this.handler.sendEmptyMessage(MSG_LOAD_ERR);
				}

			}

		}.start();
	}

	private void getOpus() {
		if ((null != moiveList)
				&& (null != moiveList.getMovieList())
				&& (!"null".equals(moiveList) && (moiveList.getMovieList()
						.size() != 0))) {
			System.out.println("集合moiveList----<<<>>>" + moiveList);
			if (pageFlag) {
				currentPage = Integer.valueOf(moiveList.getPageBean()
						.getCurrentPage());
				totalPage = Integer.valueOf(moiveList.getPageBean()
						.getTotalPage());
				totalRows = Integer.valueOf(moiveList.getPageBean()
						.getTotalRows());
				if (totalPage > 1) {
					btn_pageDown.setVisibility(View.VISIBLE);
				}
				pageFlag = false;
			}
			if (currentPage == 1) {
				btn_pageUp.setVisibility(View.INVISIBLE);
			} else if (currentPage == totalPage) {
				btn_pageDown.setVisibility(View.INVISIBLE);
			}
			tv_totalrows.setText("( 检索结果 : 共 " + totalRows + "相关作品)");
			tv_currentpage.setText("共 " + totalPage + " 页   " + "当前第 "
					+ currentPage + " 页");

			if (uRLPath.getType().equals("0")) {
				egridAdapter.setArrayList(moiveList.getMovieList());
				edu_gridview.requestFocus();
				edu_gridview.setFocusable(true);
				edu_gridview.setSelection(0);
				System.out.println("moiveList.getMovieList()"
						+ moiveList.getMovieList());
				

			} else if (uRLPath.getType().equals("1")
					|| uRLPath.getType().equals("22")|| uRLPath.getType().equals("23")) {														
				gridAdapter.changData(moiveList.getMovieList());
				gridView.requestFocus();
				gridView.setFocusable(true);
				gridView.setSelection(0);
			} else {
				otherAdapter.setSportsList(moiveList.getMovieList());
				other_gridview.requestFocus();
				other_gridview.setFocusable(true);		
				other_gridview.setSelection(0);
			}
		}
	}

	private void getInfo() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				int i = 0;
				while (true) {
					i++;
					content = HttpUtil.getResponseString(uRLPath
							.getURL_MOIVE_ALL() + currentPage + ".txt");

					myMove = HttpUtil.getResponseString(uRLPath
							.getURL_PEOPLE_ALL());

					System.out.println("mymove的下载地址为11111111"
							+ uRLPath.getURL_PEOPLE_ALL());

					System.out
							.println("相关作品contenturl"
									+ uRLPath.getURL_MOIVE_ALL() + currentPage
									+ ".txt");
					System.out.println("相关作品content返回值" + content);

					System.out.println("人物详情myMoveurl"
							+ uRLPath.getURL_PEOPLE_ALL());
					System.out.println("人物详情myMove返回值" + myMove);
					if (myMove != null) {
						return myMove;
					} else {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
							return null;
						}
					}
					if (i > 10) {
						finish();
					}
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null && result.length() != 0) {

					moiveList = JsonUtil.getMoiveList(content);
					peopleMap = JsonUtil.getpeople(myMove);
					re_movie_loading.setVisibility(View.INVISIBLE);
					layoutview.setBackgroundResource(R.drawable.people_main_bg);
					line_movie_detail.setVisibility(View.VISIBLE);
					PeopleBean people = new PeopleBean();
					people = (PeopleBean) peopleMap.get("peopleBean");
					String url = people.getPic();
					System.out.println("pic地址" + url);

					String chineseName = people.getChineseName();
					String englishName = people.getEnglishName();
					String nationality = people.getNationality();
					String constellation = people.getConstellation();
					String text = people.getSummary();

					if (!"".equals(chineseName)) { // 判断内容不为空就显示
						ll_id_chinaname.setVisibility(View.VISIBLE);
						tv_chinaname.setText(chineseName);
					}
					if (!"".equals(englishName)) {
						ll_id_foreigname.setVisibility(View.VISIBLE);
						tv_foreigname.setText(englishName);
					}
					if (!"".equals(nationality)) {
						ll_id_nationality.setVisibility(View.VISIBLE);
						tv_nationality.setText(people.getNationality());
					}
					if (!"".equals(constellation)) {
						ll_id_constellation.setVisibility(View.VISIBLE);
						tv_constellation.setText(people.getConstellation());
					}
					if (!"".equals(text)) {
						tv_id_intro.setVisibility(View.VISIBLE);
						CCmyTextView myText = new CCmyTextView(
								MainActivity.this);
						myText.setLayoutParams(new LayoutParams(710, 130));
						myText.setEllipsis("...");
						// myText.setEllipsisMore("...");
						myText.setMaxLines(4);
						myText.setLineSpacing(10, 10);
						myText.setTextSize(25);
						myText.setText(text);
						myText.setTextColor(Color.parseColor("#ffffff"));
						myText.setPadding(10, 10, 10, 10);
						// tv2.setBackgroundColor(0xFFFCDFB2);
						LinearLayout testmytextview = (LinearLayout) findViewById(R.id.testmytextview);
						testmytextview.addView(myText);
						// tv_intro.setText(text);
					}
					aQuery.find(R.id.iv_logo).image(url);
					getOpus();

				} else {
					re_movie_loading.setVisibility(View.INVISIBLE);
					finish();
				}
				super.onPostExecute(result);
			}
		}.execute();

	}

	private void pageDown() {
		if (this.currentPage >= this.totalPage) {
			btn_pageDown.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(MainActivity.this, R.string.lastPage);
			if (btn_collect.isFocused()) {
				if (uRLPath.getType().equals("0")) {
					edu_gridview.requestFocus();
					edu_gridview.setFocusable(true);
					egridAdapter.setSelctItem(-1);
				} else if (uRLPath.getType().equals("1")
						|| uRLPath.getType().equals("22")|| uRLPath.getType().equals("23")) {
					gridView.requestFocus();
					gridView.setFocusable(true);

				} else {
					other_gridview.requestFocus();
					other_gridview.setFocusable(true);
				}
			}
			return;
		}
		btn_pageUp.setVisibility(View.VISIBLE);
		this.currentPage = (1 + this.currentPage);
		System.out.println("currentPage-------" + currentPage);
		content = uRLPath.getURL_MOIVE_ALL() + currentPage + ".txt";
		this.handler.removeCallbacks(this.changDatas);
		this.handler.postDelayed(this.changDatas, 100L);
		App.playSound(getResources().getString(R.string.sound_page_change));
	}

	private void pageUp() {
		if (this.currentPage <= 1) {
			btn_pageUp.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(MainActivity.this, R.string.firstPage);
			if (btn_collect.getTag().equals("已收藏")) {
				btn_collect.setEnabled(false);
				btn_collect.setFocusable(false);
				if (uRLPath.getType().equals("0")) {
					edu_gridview.requestFocus();
					edu_gridview.setFocusable(true);
				} else if (uRLPath.getType().equals("1")
						|| uRLPath.getType().equals("22")|| uRLPath.getType().equals("23")) {
					
					gridView.requestFocus();
					gridView.setFocusable(true);
				} else {
					other_gridview.requestFocus();
					other_gridview.setFocusable(true);
				}
			} else {
				btn_collect.requestFocus();
				btn_collect.setFocusable(true);
				btn_collect.setEnabled(true);
			}
			return;
		}
		btn_collect.setEnabled(false);
		btn_collect.setFocusable(false);
		btn_pageDown.setVisibility(View.VISIBLE);
		this.currentPage = (-1 + this.currentPage);
		content = uRLPath.getURL_MOIVE_ALL() + currentPage + ".txt";
		this.handler.removeCallbacks(this.changDatas);
		this.handler.postDelayed(this.changDatas, 100L);
		App.playSound(getResources().getString(R.string.sound_page_change));
	}

	private void initListener() {
		btn_pageUp.setOnClickListener(this);
		btn_pageDown.setOnClickListener(this);

		if (uRLPath.getType().equals("0")) {
			allgridview = edu_gridview;
		} else if (uRLPath.getType().equals("1")
				|| uRLPath.getType().equals("22")|| uRLPath.getType().equals("23")) {
			allgridview = gridView;
		} else {

			allgridview = other_gridview;
		}

		allgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				String type = uRLPath.getType();
				allgridview.requestFocus();
				allgridview.setFocusable(true);//
				if (null != type && type.equals("1") || null != type
						&& type.equals("22")||null != type && type.equals("23")) { // 电影、电视剧调用
					OpusBean item = (OpusBean) gridAdapter.getItem(position); //adapter不同

					String packname = "com.ccibs.ccvideoplayer";
					String packnameMain = "com.ccibs.ccvideoplayer.MainActivity";
					String downloadpath = "http://sys.vocy.com/apk_file/137473701272560001.apk";
					callCCvideoplayer(item, packname, packnameMain,
							downloadpath, position);
				}

				else if (null != type && type.equals("0")) { // 调用音乐播放器
					OpusBean eduitem = (OpusBean) egridAdapter
							.getItem(position);
					String packname = "com.ccibs.musicplay";
					String packnameMain = "com.ccibs.musicplay.activity.MainActivity";
					String downloadpath = "null";// APK下载路径
					callCCvideoplayer(eduitem, packname, packnameMain,
							downloadpath, position);
				}
			
				else{//其它
					OpusBean item = (OpusBean) otherAdapter.getItem(position);
					
					 if (null!=type && type.equals("7")||null!=type && type.equals("11")||null!=type && type.equals("10")||null!=type && type.equals("13")) { //图片、文化、报纸、旅游频道用
						String downloadpath =null;
						String packName = null;
						String packNameMain =null;
						
						if ("游记".equals(item.getCategory())) { //旅游
							uRLPath.setDetail_id(item.getId());
							goWeb();
							return;
						}
						if ("视频".equals(item.getCategory())) {
							if (Constant.isSlientInstall) {
								 downloadpath = "http://sys.vocy.com/apk_file/137473701272560001.apk";
							}else {
								 downloadpath = "http://api.vocy.com/apk_file/137421096697960001.apk";
					     	}
							 packName="com.ccibs.ccvideoplayer";
							 packNameMain="com.ccibs.ccvideoplayer.MainActivity";
					}else if("图片".equals(item.getCategory())||"攻略".equals(item.getCategory())){
							if(Constant.isSlientInstall){
								downloadpath = "http://sys.vocy.com/apk_file/137644812677720001.apk";
							}else{
								downloadpath = "http://api.vocy.com/apk_file/137607316956910001.apk";
							}
							packName="com.ccdrive.photoviewer";
							packNameMain="com.ccdrive.photoviewer.MainActivity";
						}
						callCCvideoplayer(item,packName,packNameMain,downloadpath,position);
					 }else {
						String packname = "com.ccibs.ccvideoplayer";
						String packnameMain = "com.ccibs.ccvideoplayer.MainActivity";
						String downloadpath = "http://sys.vocy.com/apk_file/137473701272560001.apk";
						callCCvideoplayer(item, packname, packnameMain,
								downloadpath, position);
					 }
				
				}
		
			
			}
		});
		allgridview.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View itemView,
					int arg2, long arg3) {

				// if (itemView != null) {  //获取焦点后动画效果
				// itemView.startAnimation(AnimationTools
				// .getMaxAnimation(100));
				// }
				// if (preView != null) {
				// preView.startAnimation(AnimationTools
				// .getMiniAnimation(100));
				// }
				// preView = itemView;

				App.playSound(getResources().getString(  //移动声音
						R.string.sound_movie_select));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pageup:
			pageUp();
			break;
		case R.id.btn_pagedown:
			pageDown();
			break;
		case R.id.btn_collect:
			String collect = (String) btn_collect.getTag();
			if ("收藏".equals(collect)) {
				// 收藏
				//btn_collect.getTouchables();
				btn_collect.setBackgroundResource(R.drawable.cancel_selector);
				btn_collect.setTag("已收藏");
				btn_collect.setEnabled(false);
				btn_collect.setFocusable(false);
				String addFavMoviePaht = HttpRequest.getInstance()
						.getURL_COLLECT();
				addMovieFav(addFavMoviePaht);
				System.out.println("收藏返回------->" + addFavMoviePaht);
				collectApp();
				
			} 
//			else {
//				 // 点击量
//				btn_collect.setBackgroundResource(R.drawable.cancel_selector);
//				btn_collect.setTag("已收藏");
//				btn_collect.setEnabled(false);
//				btn_collect.setFocusable(false);
//				String addFavMoviePaht = HttpRequest.getInstance()
//						.getURL_CANCEL();
//				addMovieFav(addFavMoviePaht);
//			}
			break;
		}
	}

	private void addMovieFav(String path) {
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					System.out.println("收藏返回的数据为" + object);
				}
				super.callback(url, object, status);
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (uRLPath.getType().equals("0")) {

			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				if (edu_gridview.getSelectedItemId() >= 10) {
					pageDown();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

					if (edu_gridview.getSelectedItemId() <= 1) {
						pageUp();
						
					}
					
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (edu_gridview.getSelectedItemId() >= 11) {
					pageDown();
				}

			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (edu_gridview.getSelectedItemId() == 0) {
					pageUp();
				}
			}

		} else if (uRLPath.getType().equals("1")
				|| uRLPath.getType().equals("22")|| uRLPath.getType().equals("23")) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				if (gridView.getSelectedItemId() >= 6) {
					pageDown();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				if (gridView.getSelectedItemId() <= 5) {
					pageUp();
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (gridView.getSelectedItemId() >= 11) {
					pageDown();
				} else if (gridView.getSelectedItemId() == 5) {
					if (gridView.getSelectedItemId() > 5) {
						gridView.setSelection(6);
					} else {
						gridView.setSelection(5);
					}
				}

			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (gridView.getSelectedItemId() == 0) {
					pageUp();

				} else if (gridView.getSelectedItemId() == 6) {
					gridView.setSelection(5);
				}
			}

		} else {
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				if (other_gridview.getSelectedItemId() >= 4) {
					pageDown();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				if (other_gridview.getSelectedItemId() <= 3) {
					pageUp();
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (other_gridview.getSelectedItemId() >= 7) {
					pageDown();
				} else if (other_gridview.getSelectedItemId() == 3) {
					if (other_gridview.getSelectedItemId() > 3) {
						other_gridview.setSelection(4);
					} else {
						other_gridview.setSelection(3);
					}
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (other_gridview.getSelectedItemId() == 0) {
					pageUp();
				} else if (other_gridview.getSelectedItemId() == 4) {
					other_gridview.setSelection(3);
				}
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	private void startBefor() {
		layoutview = (RelativeLayout) findViewById(R.id.line_movie_main);
		layoutview.setBackgroundResource(R.drawable.menu_bg);
		line_movie_detail = (LinearLayout) findViewById(R.id.ll_line_movie_main);
		line_movie_detail.setVisibility(View.INVISIBLE);
		re_movie_loading = (RelativeLayout) findViewById(R.id.re_movie_loading);
		ImageView frame_image05 = (ImageView) findViewById(R.id.frame_image05);
		frame_image05.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.logoanmi));
		re_movie_loading.setVisibility(View.VISIBLE);
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				Message ms = new Message();
				ms.what = beginConnectData;
				handler.sendMessage(ms);
			}
		};
		timer.schedule(tt, 10);
	}

	public static void ProDiaglogDimiss(ProgressDialog dialog) {
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					// Perform action on key press
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	private void collectApp() {
		LoginUtil loginUtil = new LoginUtil(MainActivity.this);
		String mac = loginUtil.getWifiMacAddress(MainActivity.this)
				.toUpperCase();
		SoapUtils soapUtils = SoapUtils.getInstance();
		HashMap<String, Object> appMap = new HashMap<String, Object>();
		appMap.put("mac", mac);
		appMap.put("orderId", id);
		soapUtils.log("PeopleCollect", appMap);
		long peopleid = Long.parseLong(id);
		soapUtils.getPeopleClick(peopleid);
	}

	private void downloadImage(String url, final ImageView iamgeView) {
		aQuery.ajax(url, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				super.callback(url, object, status);
				if (null != object || !"".equals(object)) {
					aQuery.find(R.id.iv_logo).image(object);
				}
				iamgeView.setImageResource(R.drawable.grid_item_default);
			}
		});
	}
	private void callCCvideoplayer(OpusBean item, final String packname,
			String packnameMain, final String downloadpath, int position) {
		AppUtil au = new AppUtil(aQuery.getContext());
		if (!au.isInstall(packname)) {
			final Dialog dialog = new Dialog(MainActivity.this,
					R.style.Theme_Dialog);
			dialog.setContentView(R.layout.exitdialog);
			Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
			Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
			TextView tv_dialog = (TextView) dialog.findViewById(R.id.tv_dialog);
			tv_dialog.setText("需要下载播放器,是否下载?");
			btn_yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Handler handler = new Handler();
					final Dialog dialogbar = new Dialog(MainActivity.this,
							R.style.Theme_Dialog);
					dialogbar.setContentView(R.layout.activity_progressbar);
					pb_bar = (ProgressBar) dialogbar.findViewById(R.id.pb_bar);
					tv_progressbar = (TextView) dialogbar
							.findViewById(R.id.tv_progressbar);
					dialogbar.show();
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							UpdateVersion uv = UpdateVersion.instance(
									aQuery.getContext(), handler, true);
							uv.setLoadApkName(packname);
							uv.setUpdateUrl(downloadpath);
							uv.setProgressBar(pb_bar, dialogbar, tv_progressbar);
							uv.run();
							return null;
						}
					}.execute();
					dialog.dismiss();
				}
			});
			btn_no.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			if (uRLPath.getType().equals("0")) {
				Intent intent = new Intent();
				intent.putExtra("id", item.getId());
				intent.putExtra("peopleid", id);
				intent.putExtra("webRoot", uRLPath.getWEB_ROOT());
				intent.putExtra("type", uRLPath.getType());
				intent.putExtra("pageSize", uRLPath.getPagesize());
				intent.putExtra("currpage", currentPage);
				intent.putExtra("position", position);
				intent.putExtra("flag", "0_1");
				intent.setClassName(packname, packnameMain);
				startActivity(intent);
				System.out.println("位置为position---->" + position);
				System.out.println("位置为人物ID---->" + id);
			} else {
				Intent intent = new Intent();
				if (uRLPath.getType().equals("23")||uRLPath.getType().equals("7")||uRLPath.getType().equals("11")||uRLPath.getType().equals("10")) { //动漫、图片、文化、报纸频道
				intent.putExtra("kind", item.getCategory());
				}
				intent.putExtra("webRoot", uRLPath.getWEB_ROOT());
				intent.putExtra("id", item.getId());
				intent.putExtra("type", uRLPath.getType());
				intent.putExtra("mytoken", HttpRequest.getInstance()
						.getMytoken());
				intent.setClassName(packname, packnameMain);
				startActivity(intent);
			}
		}
	}
		//旅游 游记调用
		private void goWeb(){
			 AQuery aQuery = new AQuery(context);
			 String url = uRLPath.getURL_DETAIL();
			 aQuery.ajax(url, String.class, new AjaxCallback<String>()
					 {
						@Override
						public void callback(String url, String object,
								AjaxStatus status) {
							if(object!=null){
								try {
									JSONArray jsonArray = new JSONArray(object);
									JSONObject json = jsonArray.getJSONObject(0);
									if(!json.isNull("htmlpath")){
										String webPath=(String) json.get("htmlpath");
										Intent intent = new Intent();        
										intent.setAction("android.intent.action.VIEW");    
										Uri content_url = Uri.parse(webPath);   
										intent.setData(content_url);  
										startActivity(intent);
									}else{
										Toast.makeText(context, "无网页!", 1000).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
				 
					 }
					);
	}

}
