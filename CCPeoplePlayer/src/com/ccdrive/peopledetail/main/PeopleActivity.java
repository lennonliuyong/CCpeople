package com.ccdrive.peopledetail.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.peopledetail.adapter.Constant;
import com.ccdrive.peopledetail.adapter.PeopleGridAdapter;
import com.ccdrive.peopledetail.adapter.TypeDetailsSubMenuAdapter;
import com.ccdrive.peopledetail.app.App;
import com.ccdrive.peopledetail.bean.PeopleBean;
import com.ccdrive.peopledetail.bean.PeopleList;
import com.ccdrive.peopledetail.bean.TypeBean;
import com.ccdrive.peopledetail.http.HttpRequest;
import com.ccdrive.peopledetail.util.AnimationTools;
import com.ccdrive.peopledetail.util.HttpUtil;
import com.ccdrive.peopledetail.util.JsonUtil;
import com.ccdrive.peopledetail.util.ToastUtil;
import com.ccdrive.peopledetail.util.UpdateVersion;

public class PeopleActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private View searchLayout;
	private View search2Layout;
	private GridView gridView;
	private PeopleGridAdapter gridAdapter;
	private Button btn_pageUp;
	private Button btn_pageDown;
	private Button btn_submit_type;
	private Button btn_toSearch;
	private Button btn_search;
	private TextView tv_currentpage;
	private TextView tv_totalrows;
	private TextView tv_type;
	private EditText et_search;
	private HashMap<String, String> paramMap;
	private PeopleList peopleList;
	private HttpRequest uRLPath;
	private Handler handler;
	private int currentPage = 1;
	private int totalPage;
	private int totalRows;
	private boolean pageFlag = true;
	private boolean searchFlag = false;// 检索或关键字查询
	private String strPreType = "";
	private String strTempType = "";
	private View preView = null;
	private String path;
	private int collectFlag;
	private String dataPath;
	private Context context;
	private ImageView img_menu;
	private LinearLayout layTypeList;
	private ProgressBar pb_bar;
	private TextView tv_progressbar;
	private LayoutAnimationController animationController;
	private HashMap<String, String> typeHashMap;
	private Runnable changDatas = new Runnable() {
		@Override
		public void run() {
			handler.sendEmptyMessage(Constant.MSG_LOAD_MOVIE);
		}
	};
	private String people;
	protected ArrayList<String> typeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_main);
		this.context = PeopleActivity.this;
		initObject();
		initView();
		initListener();
		initData();
		initMenuData();
	}

	public void initView() {
		tv_totalrows = (TextView) findViewById(R.id.tv_totalrows);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_currentpage = (TextView) findViewById(R.id.tv_currentpage);
		btn_pageUp = (Button) findViewById(R.id.btn_pageup);
		btn_pageDown = (Button) findViewById(R.id.btn_pagedown);
		btn_submit_type = (Button) findViewById(R.id.btn_submit_type);
		btn_toSearch = (Button) findViewById(R.id.btn_toSearch);
		btn_search = (Button) findViewById(R.id.btn_search);
		et_search = (EditText) findViewById(R.id.search_edit);
		layTypeList = (LinearLayout) findViewById(R.id.typelist);
		System.out.println("layTypeList.getId()----->" + layTypeList.getId());
		et_search.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				10) });
		img_menu = (ImageView) findViewById(R.id.menu_img);
		AnimationTools.setMenuAnimation(img_menu, R.drawable.menu_key,
				R.drawable.menu_key_b);
		searchLayout = (View) findViewById(R.id.type_details_menulayout);
		search2Layout = (View) findViewById(R.id.search_menu);
		gridView = (GridView) findViewById(R.id.grid_list);
		gridView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		gridView.setSelector(new ColorDrawable(0));
		gridAdapter = new PeopleGridAdapter(context, null, gridView);
		gridView.setAdapter(this.gridAdapter);
		gridView.setFocusableInTouchMode(true);
	}

	public void initObject() {
		uRLPath = HttpRequest.getInstance();
		checkVersion();
		// intent.putExtra("type", "1");
		// intent.putExtra("webRoot", httpRequest.getWEB_ROOT());
		// intent.putExtra("myToken", httpRequest.getMytoken());
		// intent.putExtra("collectFlag", Constant.STORE);
		// intent.putExtra("webRootDetail", webRootDetail);
		Intent intent = getIntent();
		people = intent.getStringExtra("type");
		String webRoot = intent.getStringExtra("webRoot");
		String webRootDetail = intent.getStringExtra("webRootDetail");
		String myToken = intent.getStringExtra("myToken");
		String webserviceurl = intent.getStringExtra("webserviceurl");
		uRLPath.setType(people);
		uRLPath.setWEB_ROOT(webRoot);
		uRLPath.setWebRootDetail(webRootDetail);
		System.out.println("webRootDetailwebRootDetail----->" + webRootDetail);
		System.out.println("webRootwebRootwebRoot----->" + webRootDetail);
		uRLPath.setMyToken(myToken);
		collectFlag = intent.getIntExtra("collectFlag", -1);

		if (collectFlag == Constant.STORE) {
			dataPath = uRLPath.getURL_PEOPLE_ALL_MOVIESHOP() + currentPage;
		} else {
			dataPath = uRLPath.getURL_List_COLLECT() + currentPage;
			// dataPath=
			// "http://192.168.1.3:2014/html/actor/actor_1_137638494991000002.txt";

		}
		System.out.println("根据URL判断是商店还是收藏------>" + dataPath);
		paramMap = new HashMap<String, String>();
		animationController = new LayoutAnimationController(
				AnimationTools.alphaAndTrans(), 0.5f);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.MSG_LOAD_MOVIEINFO:
					loadData();
					break;
				case Constant.MSG_LOAD_MOVIE:
					initData();
					break;
				case Constant.MSG_SHOW_FLITER:
					setFilterMessage();
					break;
				case Constant.MSG_LOAD_ERR:
					Toast.makeText(context, R.string.load_error, 0).show();
					break;
				case Constant.MSG_LOAD_NODATA:
					gridAdapter.changData(null);
					tv_totalrows.setText("");
					tv_currentpage.setText("");
					break;
				default:
					break;
				}
			}

		};
	}

	public void initData() {
		new Thread() {
			@Override
			public void run() {
				String respose = HttpUtil.getResponseString(dataPath);
				System.out.println("人物列表dataPath-------------------->"
						+ dataPath);
				System.out
						.println("人物列表respose-------------------->" + respose);
				if (null != respose && !"".equals(respose)) {
					peopleList = JsonUtil.getPeopleList(respose);
					if (peopleList != null
							&& peopleList.getPeopleList() != null
							&& peopleList.getPeopleList().size() != 0) {
						handler.sendEmptyMessage(Constant.MSG_LOAD_MOVIEINFO);
						return;
					} else if (peopleList.getPageBean() != null) {
						handler.sendEmptyMessage(Constant.MSG_LOAD_NODATA);

						return;
					}
					handler.sendEmptyMessage(Constant.MSG_LOAD_ERR);
				}
			}
		}.start();
	}

	public void initMenuData() {
		new Thread() {
			@Override
			public void run() {
				System.out.println("menu----------------------------->"
						+ uRLPath.getURL_PEOPLE_SEARCH());
				String respose = HttpUtil.getResponseString(uRLPath
						.getURL_PEOPLE_SEARCH());
				if (null != respose && !"".equals(respose)) {
					final LayoutInflater inflater = LayoutInflater
							.from(PeopleActivity.this);
					final ArrayList<HashMap<String, Object>> menuList = JsonUtil
							.getMenuList(respose);
					typeList = new ArrayList<String>();
					handler.post(new Runnable() {

						@Override
						public void run() {

							for (int i = 0, m = menuList.size(); i < m; i++) {
								HashMap<String, Object> hashMap = menuList
										.get(i);
								TypeBean typeBean = (TypeBean) hashMap
										.get("typeBean");
								View view = inflater.inflate(
										R.layout.menu_item, null);
								TextView title = (TextView) view
										.findViewById(R.id.menu_item_type);
								title.setText(typeBean.getName());
								typeList.add(typeBean.getColumns());
								ListView listView = (ListView) view
										.findViewById(R.id.filter_list_type);
								listView.setId(3000 + i);
								ArrayList<HashMap<String, String>> menuDataList = (ArrayList<HashMap<String, String>>) hashMap
										.get("menuData");
								TypeDetailsSubMenuAdapter typeAdapter = new TypeDetailsSubMenuAdapter(
										PeopleActivity.this, typeBean
												.getColumns(), menuDataList);
								listView.setAdapter(typeAdapter);
								layTypeList.addView(view);
								listView.setNextFocusDownId(listView.getId());
								listView.setNextFocusUpId(listView.getId());
								if (i > 0) {
									listView.setNextFocusLeftId(listView
											.getId() - 1);
								}
								if (i < m - 1) {
									listView.setNextFocusRightId(listView
											.getId() + 1);
								}
								listView.setOnItemClickListener(PeopleActivity.this);
							}
							typeHashMap = new HashMap<String, String>();
						}
					});
				}
			}

		}.start();

	}

	public void initListener() {
		btn_pageUp.setOnClickListener(this);
		btn_pageDown.setOnClickListener(this);
		btn_submit_type.setOnClickListener(this);
		btn_toSearch.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		img_menu.setOnClickListener(this);
		gridView.setLayoutAnimation(animationController);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PeopleBean item = gridAdapter.getItem(arg2);
				Intent intent = new Intent(PeopleActivity.this,
						MainActivity.class);
				intent.putExtra("id", item.getId());
				intent.putExtra("people", people);
				context.startActivity(intent);
			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (collectFlag == Constant.COLLECT) {
					final PeopleBean item = gridAdapter.getItem(arg2);
					final AQuery aQuery = new AQuery(context);
					Dialog dialog = new AlertDialog.Builder(context)
							.setTitle(
									context.getResources().getString(
											R.string.pmt))
							.setMessage(
									context.getResources().getString(
											R.string.uncollect))
							.setPositiveButton(
									context.getResources().getString(
											R.string.confirm),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String collect_cancel = uRLPath
													.getURL_CANCEL()
													+ item.getId();
											System.out
													.println("长按取消收藏URL-------->>>>"
															+ collect_cancel);
											aQuery.ajax(collect_cancel,
													String.class,
													new AjaxCallback<String>() {
														@Override
														public void callback(
																String url,
																String jsonResult,
																AjaxStatus status) {
															// TODO
															// Auto-generated
															// method stub
															super.callback(url,
																	jsonResult,
																	status);
															System.out
																	.println("长按取消收藏URL"
																			+ url);
															if (gridView
																	.getChildCount() == 0
																	&& PeopleActivity.this.currentPage > 1) {
																PeopleActivity.this.currentPage = (-1 + PeopleActivity.this.currentPage);
															}
															System.out.println("长按删除后的url------>"
																	+ uRLPath
																			.getURL_List_COLLECT()
																	+ currentPage);

															PeopleActivity.this.dataPath = dataPath;
															PeopleActivity.this.pageFlag = true;
															PeopleActivity.this
																	.initData();
															Toast.makeText(
																	context,
																	R.string.collect_cancel_success,
																	1000)
																	.show();
														}
													});
										}
									})
							.setNegativeButton(
									context.getString(R.string.back),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {

										}
									}).create();
					dialog.show();
				}
				return true;
			}
		});
		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// arg1.startAnimation(AnimationTools.getMaxAnimation(100));
				// if (preView != null)
				// {
				// preView.startAnimation(AnimationTools.getMiniAnimation(100));
				// }
				// preView = arg1;
				App.playSound(getResources().getString(
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
		case R.id.btn_submit_type:
			strPreType = strTempType;
			tv_type.setText(strPreType);
			searchLayout.setVisibility(View.GONE);
			searchLayout.clearFocus();
			gridView.setEnabled(true);
			gridView.setFocusable(true);
			currentPage = 1;
			pageFlag = true;
			if (collectFlag == Constant.STORE) {
				dataPath = uRLPath.getURL_PEOPLE_ALL_MOVIESHOP() + currentPage
						+ setMenuParms();
			} else {
				dataPath = uRLPath.getURL_List_COLLECT() + currentPage
						+ setMenuParms();
			}
			// String submitpath = dataPath + setMenuParms();
			System.out.println("检索dataPath-------------------------->"
					+ dataPath);
			this.handler.removeCallbacks(this.changDatas);
			this.handler.postDelayed(this.changDatas, 100L);
			App.playSound(getResources().getString(R.string.sound_page_change));
			break;
		case R.id.btn_toSearch:
			searchLayout.setVisibility(View.GONE);
			searchLayout.clearFocus();
			gridView.clearFocus();
			gridView.setFocusable(false);
			gridView.setEnabled(false);
			search2Layout.setVisibility(View.VISIBLE);
			search2Layout.requestFocus();
			search2Layout.setFocusable(true);
			break;
		case R.id.btn_search:
			// 关闭软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
			searchFlag = true;
			currentPage = 1;
			pageFlag = true;
			//
			if (collectFlag == Constant.STORE) {
				dataPath = uRLPath.getURL_PEOPLE_ALL_MOVIESHOP() + currentPage
						+ setMenuParms();
				System.out.println("关键字搜索------->" + dataPath);

			} else {
				dataPath = uRLPath.getURL_List_COLLECT() + currentPage
						+ setMenuParms();
			}
			this.handler.removeCallbacks(this.changDatas);
			this.handler.postDelayed(this.changDatas, 100L);
			strPreType = et_search.getText().toString();
			tv_type.setText("关键字 >>" + strPreType);
			search2Layout.clearFocus();
			search2Layout.setVisibility(View.GONE);
			gridView.setEnabled(true);
			gridView.requestFocus();
			gridView.setFocusable(true);
			break;
		case R.id.menu_img:
			doSearch();
			break;
		}

	}

	public void loadData() {
		System.out.println("peopleList人物人物--->" + peopleList);
		if (null != peopleList && null != peopleList.getPeopleList()
				&& peopleList.getPeopleList().size() != 0
				&& !"null".equals("peopleList")) {
			System.out.println("peopleList人物人物--->" + peopleList);
			if (pageFlag) {
				currentPage = Integer.valueOf(peopleList.getPageBean()
						.getCurrentPage());
				totalPage = Integer.valueOf(peopleList.getPageBean()
						.getTotalPage());
				totalRows = Integer.valueOf(peopleList.getPageBean()
						.getTotalRows());
				if (totalPage > 1) {
					btn_pageDown.setVisibility(View.VISIBLE);
				} else {
					btn_pageDown.setVisibility(View.INVISIBLE);
				}

				pageFlag = false;
			}
			if (currentPage == 1) {
				btn_pageUp.setVisibility(View.INVISIBLE);
			} else if (currentPage == totalPage) {
				btn_pageDown.setVisibility(View.INVISIBLE);
			}
			tv_totalrows.setText("( 共 " + totalRows + "个相关人物 )");
			tv_currentpage.setText("共 " + totalPage + " 页   " + "当前第 "
					+ currentPage + " 页");
			gridAdapter.changData(peopleList.getPeopleList());
			gridView.requestFocus();
			gridView.setSelection(0);
		}
	}

	private void pageDown() {
		if (this.currentPage >= this.totalPage) {
			btn_pageDown.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(context, R.string.lastPage);
			return;
		}
		btn_pageUp.setVisibility(View.VISIBLE);
		this.currentPage = (1 + this.currentPage);
		//
		dataPath = uRLPath.getURL_PEOPLE_ALL_MOVIESHOP() + currentPage
				+ setMenuParms();
		this.handler.removeCallbacks(this.changDatas);
		this.handler.postDelayed(this.changDatas, 100L);
		gridView.setLayoutAnimation(animationController);
		App.playSound(getResources().getString(R.string.sound_page_change));
	}

	private void pageUp() {
		if (this.currentPage <= 1) {
			btn_pageUp.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(context, R.string.firstPage);
			return;
		}
		btn_pageDown.setVisibility(View.VISIBLE);
		this.currentPage = (-1 + this.currentPage);
		//
		dataPath = uRLPath.getURL_PEOPLE_ALL_MOVIESHOP() + currentPage
				+ setMenuParms();
		this.handler.removeCallbacks(this.changDatas);
		this.handler.postDelayed(this.changDatas, 100L);
		gridView.setLayoutAnimation(animationController);
		App.playSound(getResources().getString(R.string.sound_page_change));
	}

	private void setFilterMessage() {
		strTempType = "";
		StringBuilder builder = new StringBuilder();
		Set<Entry<String, String>> entrySet = typeHashMap.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			builder.append("|" + iterator.next().getValue());
		}
		tv_type.setText(">> " + builder.toString());
		strTempType = ">> " + builder.toString();
	}

	private void doSearch() {
		searchFlag = false;
		if (searchLayout.getVisibility() == View.GONE
				&& search2Layout.getVisibility() == View.GONE) {
			searchLayout.setVisibility(View.VISIBLE);
			gridView.clearFocus();
			gridView.setFocusable(false);
			gridView.setEnabled(false);
			searchLayout.requestFocus();

		} else {
			tv_type.setText(strPreType);
			searchLayout.setVisibility(View.GONE);
			searchLayout.clearFocus();
			search2Layout.setVisibility(View.GONE);
			search2Layout.clearFocus();
			gridView.setEnabled(true);
			gridView.setFocusable(true);
		}
	}

	private String setMenuParms() {
		if (searchFlag) {
			return "&name="
					+ HttpUtil.changeRecode(et_search.getText().toString());
		}
		Iterator<Entry<String, String>> iterator = paramMap.entrySet()
				.iterator();
		StringBuilder stringBuilder = new StringBuilder();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			String param = "&" + next.getKey() + "=" + next.getValue();
			stringBuilder.append(param);
		}
		return stringBuilder.toString();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		((TypeDetailsSubMenuAdapter) parent.getAdapter())
				.setSelctItem(position);
		HashMap<String, String> map = ((TypeDetailsSubMenuAdapter) parent
				.getAdapter()).getItem(position);
		String columns = ((TypeDetailsSubMenuAdapter) parent.getAdapter())
				.getColumns();
		Set<Entry<String, String>> entrySet = map.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		Entry<String, String> next = iterator.next();
		typeHashMap.put(columns, next.getKey());
		if ("全部".equals(next.getKey())) {
			paramMap.remove(columns);
		} else {
			paramMap.put(columns, next.getValue());
		}
		handler.sendEmptyMessage(Constant.MSG_SHOW_FLITER);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if ((gridView.getSelectedItemId()) >= 6
					&& (searchLayout.getVisibility() == View.GONE)
					&& (search2Layout.getVisibility() == View.GONE)) {
				pageDown();
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if ((gridView.getSelectedItemId() <= 5)
					&& (searchLayout.getVisibility() == View.GONE)
					&& (search2Layout.getVisibility() == View.GONE)) {
				pageUp();
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if ((gridView.getSelectedItemId() == 6)
					&& (searchLayout.getVisibility() == View.GONE)
					&& (search2Layout.getVisibility() == View.GONE)) {
				gridView.setSelection(5);
			} else if ((gridView.getSelectedItemId() == 0)
					&& (searchLayout.getVisibility() == View.GONE)
					&& (search2Layout.getVisibility() == View.GONE)) {
				pageUp();
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if ((gridView.getSelectedItemId() == 5 && (searchLayout
					.getVisibility() == View.GONE))
					&& (search2Layout.getVisibility() == View.GONE)) {
				gridView.setSelection(6);

			} else if ((gridView.getSelectedItemId() == 11 && (searchLayout
					.getVisibility() == View.GONE))
					&& (search2Layout.getVisibility() == View.GONE)) {
				pageDown();
			}
		} else if (keyCode == Constant.KEYCODE_SERCH) {
			doSearch();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void checkVersion() {
		try {
			AQuery aQuery = new AQuery(PeopleActivity.this);
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfos = packageManager.getPackageInfo(
					getPackageName(), 0);
			final String version = packInfos.versionName;
			String packageName = packInfos.packageName;
			System.out.println("packageName" + packageName);
			String apkPath = HttpRequest.getInstance().getURL_UPDATE_APK()
					+ packageName;
			aQuery.ajax(apkPath, String.class, new AjaxCallback<String>() {
				@Override
				public void callback(String url, String apkStr,
						AjaxStatus status) {
					System.out.println("更新的地址URL为" + url);
					System.out.println("更新的地址为" + apkStr);

					if (apkStr != null && apkStr.length() != 0) {
						// TODO Auto-generated method stub
						super.callback(url, apkStr, status);
						// 动漫[TAB]com.ccdrive.comic[TAB]1.1.2.9[TAB]137048843775650001[CR]
						/**
						 * apkEntity[0]=动漫 apkEntity[1]=com.ccdrive.comic
						 * apkEntity[2]=1.1.2.9 apkEntity[3]=137048843775650001
						 */
						String[] apkEntity = apkStr.split("\\[TAB\\]|\\[CR\\]");
						if (!version.equals(apkEntity[2])) {
							HttpRequest.getInstance().setApkuuid(apkEntity[3]);
							String path = HttpRequest.getInstance()
									.getURL_DOWN_UPDATE_APK();
							setUpdateDiago(path, apkEntity[1]);
						}
					}
				}
			});
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	void setUpdateDiago(final String path, final String apkName) {
		final Handler hd = new Handler();
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(PeopleActivity.this,	R.style.Theme_Dialog);
		dialog.setContentView(R.layout.exitdialog);
		Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
		Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
		btn_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
		  final ProgressDialog pdialog = new ProgressDialog(PeopleActivity.this, R.style.Theme_Dialog);
				pdialog.setContentView(R.layout.activity_progressbar);
				pb_bar = (ProgressBar) pdialog.findViewById(R.id.pb_bar);
				tv_progressbar = (TextView) pdialog.findViewById(R.id.tv_progressbar);
				pdialog.setIndeterminate(true);
				pdialog.show();
				
//				final Dialog dialogbar = new Dialog(PeopleActivity.this,
//						R.style.Theme_Dialog);
//				dialogbar.setContentView(R.layout.activity_progressbar);
//				pb_bar = (ProgressBar) dialogbar.findViewById(R.id.pb_bar);
//				tv_progressbar = (TextView) dialogbar.findViewById(R.id.tv_progressbar);
//				dialogbar.show();
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						UpdateVersion uv = UpdateVersion.instance(
								PeopleActivity.this, hd, true, false);
						uv.setUpdateUrl(path);
						uv.setProgressBar(pb_bar, pdialog, tv_progressbar);
						uv.setLoadApkName(apkName);
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
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}

}
